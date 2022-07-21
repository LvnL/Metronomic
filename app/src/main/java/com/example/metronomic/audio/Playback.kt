package com.example.metronomic.audio

import android.media.SoundPool

class Playback(soundPool: SoundPool, soundID: Int) {
    private val soundPool: SoundPool
    private val soundID: Int

    init {
        this.soundPool = soundPool
        this.soundID = soundID
    }

    fun playSound() {
        soundPool.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f)
    }
}