package com.asana.timer.ui

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.savedstate.SavedStateRegistryOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.viewModelScope
import com.asana.timer.data.Asana
import com.asana.timer.data.AsanaRepository
import com.asana.timer.data.AsanaSequence
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel(
    private val repository: AsanaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sequenceId: String? = savedStateHandle.get<String>(TimerDestination.SEQUENCE_ID_ARG)

    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var tickerJob: Job? = null

    init {
        val id = sequenceId
        if (id.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Sequenz konnte nicht geladen werden.") }
        } else {
            observeSequence(id)
        }
    }

    companion object {
        fun provideFactory(
            repository: AsanaRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): Factory = object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return TimerViewModel(repository, handle) as T
            }
        }
    }

    private fun observeSequence(id: String) {
        viewModelScope.launch {
            repository.sequences.collect { allSequences ->
                val sequence = allSequences.find { it.id == id }
                if (sequence == null) {
                    stopTicker()
                    _uiState.value = TimerUiState(errorMessage = "Sequenz nicht gefunden")
                } else {
                    stopTicker()
                    _uiState.value = TimerUiState(sequence = sequence)
                }
            }
        }
    }

    fun startOrResume() {
        val state = _uiState.value
        val sequence = state.sequence ?: return
        if (sequence.asanas.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Diese Sequenz enthält keine Asanas.") }
            return
        }

        val nextState = if (state.currentIndex == -1 || state.isCompleted) {
            state.copy(
                currentIndex = 0,
                remainingSeconds = sequence.asanas.first().durationSeconds,
                isRunning = true,
                isCompleted = false,
                errorMessage = null
            )
        } else {
            state.copy(isRunning = true, errorMessage = null)
        }

        _uiState.value = nextState
        ensureTicker()
    }

    fun pause() {
        if (_uiState.value.isRunning) {
            _uiState.update { it.copy(isRunning = false) }
            stopTicker()
        }
    }

    fun reset() {
        stopTicker()
        _uiState.update { state ->
            state.copy(
                currentIndex = -1,
                remainingSeconds = 0,
                isRunning = false,
                isCompleted = false,
                errorMessage = null
            )
        }
    }

    fun skipForward() {
        moveToNext(manual = true)
    }

    fun skipBackward() {
        val state = _uiState.value
        val sequence = state.sequence ?: return
        if (sequence.asanas.isEmpty()) return

        val previousIndex = when {
            state.currentIndex <= 0 -> -1
            else -> state.currentIndex - 1
        }

        val updated = when (previousIndex) {
            -1 -> state.copy(
                currentIndex = -1,
                remainingSeconds = 0,
                isRunning = false,
                isCompleted = false
            )

            else -> state.copy(
                currentIndex = previousIndex,
                remainingSeconds = sequence.asanas[previousIndex].durationSeconds,
                isRunning = state.isRunning,
                isCompleted = false
            )
        }

        _uiState.value = updated
        if (updated.isRunning) {
            ensureTicker()
        }
    }

    fun acknowledgeError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun ensureTicker() {
        if (tickerJob?.isActive == true) return
        tickerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                onTick()
            }
        }
    }

    private fun onTick() {
        val state = _uiState.value
        val sequence = state.sequence ?: return
        if (!state.isRunning || state.currentIndex == -1) return

        val remaining = state.remainingSeconds - 1
        if (remaining > 0) {
            _uiState.update { it.copy(remainingSeconds = remaining) }
        } else {
            moveToNext(manual = false)
        }
    }

    private fun moveToNext(manual: Boolean) {
        val state = _uiState.value
        val sequence = state.sequence ?: return
        if (sequence.asanas.isEmpty()) return

        val nextIndex = when {
            state.currentIndex == -1 -> 0
            else -> state.currentIndex + 1
        }

        if (nextIndex >= sequence.asanas.size) {
            stopTicker()
            _uiState.update {
                it.copy(
                    currentIndex = sequence.asanas.lastIndex,
                    remainingSeconds = 0,
                    isRunning = false,
                    isCompleted = true
                )
            }
        } else {
            val shouldRun = if (manual) state.isRunning else true
            _uiState.update {
                it.copy(
                    currentIndex = nextIndex,
                    remainingSeconds = sequence.asanas[nextIndex].durationSeconds,
                    isRunning = shouldRun,
                    isCompleted = false,
                    errorMessage = null
                )
            }
            if (shouldRun) {
                ensureTicker()
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTicker()
    }
}

data class TimerUiState(
    val sequence: AsanaSequence? = null,
    val currentIndex: Int = -1,
    val remainingSeconds: Int = 0,
    val isRunning: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
) {
    val currentAsana: Asana? = sequence?.asanas?.getOrNull(currentIndex)
    val nextAsana: Asana? = sequence?.asanas?.getOrNull(currentIndex + 1)

    val totalDurationSeconds: Int = sequence?.asanas?.sumOf { it.durationSeconds } ?: 0

    val elapsedSeconds: Int
        get() {
            val seq = sequence ?: return 0
            if (currentIndex < 0) return 0
            val completed = seq.asanas.take(currentIndex).sumOf { it.durationSeconds }
            val current = (currentAsana?.durationSeconds ?: 0) - remainingSeconds
            return (completed + current.coerceAtLeast(0)).coerceAtLeast(0)
        }

    val progress: Float
        get() {
            val total = totalDurationSeconds
            if (total == 0) return 0f
            return (elapsedSeconds.toFloat() / total.toFloat()).coerceIn(0f, 1f)
        }

    val isReady: Boolean = sequence?.asanas?.isNotEmpty() == true
}

object TimerDestination {
    const val route = "timer/{sequenceId}"
    const val SEQUENCE_ID_ARG = "sequenceId"

    fun createRoute(sequenceId: String): String = "timer/$sequenceId"
}

