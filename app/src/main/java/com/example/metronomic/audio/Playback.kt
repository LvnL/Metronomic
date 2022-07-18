package com.example.metronomic.audio

import android.media.SoundPool
import com.example.metronomic.timing.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Playback(eventBus: EventBus, soundPool: SoundPool, soundID: Int) {
    private val eventBus: EventBus
    private val soundPool: SoundPool
    private val soundID: Int
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        this.eventBus = eventBus
        this.soundPool = soundPool
        this.soundID = soundID
        scope.launch {
            eventBus.events.collectLatest { playSound() }
        }
    }

    private fun playSound() {
        soundPool.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f)
    }
}