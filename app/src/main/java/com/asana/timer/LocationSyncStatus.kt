package com.asana.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class LocationSyncState {
    UNKNOWN,
    SUCCESS,
    FAILURE
}

object LocationSyncStatus {
    private val _status = MutableStateFlow(LocationSyncState.UNKNOWN)
    val status: StateFlow<LocationSyncState> = _status

    private val _lastErrorMessage = MutableStateFlow<String?>(null)
    val lastErrorMessage: StateFlow<String?> = _lastErrorMessage

    fun markSuccess() {
        _status.value = LocationSyncState.SUCCESS
        _lastErrorMessage.value = null
    }

    fun markFailure(message: String? = null) {
        _status.value = LocationSyncState.FAILURE
        _lastErrorMessage.value = message
    }
}

