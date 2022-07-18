package com.example.metronomic.timing

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBus {
    private val _events = MutableSharedFlow<TimeEmitter>()
    val events = _events.asSharedFlow()

    suspend fun invokeEvent(timeEmitter: TimeEmitter) = _events.emit(timeEmitter)
}