package com.asana.timer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asana.timer.data.AsanaRepository
import com.asana.timer.data.AsanaSequence
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AsanaViewModel(
    private val repository: AsanaRepository
) : ViewModel() {

    val uiState: StateFlow<AsanaListUiState> = repository.sequences
        .map { sequences ->
            AsanaListUiState(sequences.sortedBy { it.name.lowercase() })
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AsanaListUiState(emptyList())
        )

    fun upsert(sequence: AsanaSequence) {
        viewModelScope.launch {
            repository.upsertSequence(sequence)
        }
    }

    fun delete(sequenceId: String) {
        viewModelScope.launch {
            repository.deleteSequence(sequenceId)
        }
    }
}

data class AsanaListUiState(
    val sequences: List<AsanaSequence>
)

