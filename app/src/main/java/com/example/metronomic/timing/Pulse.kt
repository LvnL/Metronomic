package com.example.metronomic.timing

import android.media.SoundPool
import com.example.metronomic.audio.Playback

class Pulse(eventBus: EventBus, soundPool: SoundPool, soundID: Int) : TimeEmitter(eventBus) {
    override val playback = Playback(eventBus, soundPool, soundID)
    override val type = Type.PULSE

    var bpm: Int = 124
    var pulseIsRunning: Boolean = false

    // IOI = Interonset Interval
    private var ioiMS: Double = 60000.toDouble() / bpm
    private var ioiMSRounded: Long = ioiMS.toLong()
    private var roundingDifferenceMS: Double = ioiMS - ioiMSRounded
    private var roundingDifferenceNS: Double = roundingDifferenceMS * 1000000
    private var roundingDifferenceNSRounded: Int = roundingDifferenceNS.toInt()

    fun stopPulse() {
        pulseIsRunning = false
    }

    suspend fun startPulse() {
        pulseIsRunning = true
        while (pulseIsRunning) {
            click()
            ioiMS = 60000.toDouble() / bpm
            ioiMSRounded = ioiMS.toLong()
            roundingDifferenceMS = ioiMS - ioiMSRounded
            roundingDifferenceNS = roundingDifferenceMS * 1000000
            roundingDifferenceNSRounded = roundingDifferenceNS.toInt()
            Thread.sleep(ioiMSRounded, roundingDifferenceNSRounded)
        }
    }
}