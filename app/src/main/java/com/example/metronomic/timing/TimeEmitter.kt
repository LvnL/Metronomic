package com.example.metronomic.timing

import com.example.metronomic.audio.Playback

abstract class TimeEmitter(eventBus: EventBus) {
    abstract val playback: Playback
    abstract val type: Type
    private val eventBus: EventBus

    init {
        this.eventBus = eventBus
    }

    suspend fun click() = eventBus.invokeEvent(this)
}

enum class Type {
    PULSE,
    SUBDIVISION
}