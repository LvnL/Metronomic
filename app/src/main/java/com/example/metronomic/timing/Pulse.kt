package com.example.metronomic.timing

import android.media.SoundPool
import com.example.metronomic.audio.Playback
import kotlinx.coroutines.Runnable
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class Pulse(soundPool: SoundPool, soundID: Int) : TimeEmitter() {
    override val playback = Playback(soundPool, soundID)
    override val scheduledThreadPoolExecutor = ScheduledThreadPoolExecutor(4)
    override val type = Type.PULSE

    var bpm: Int = 124
    var isRunning: Boolean = false
    val subdivisions: ArrayList<Subdivision> = ArrayList()

    private var scheduledFuture: ScheduledFuture<*>? = null
    private var runnable: Runnable = Runnable {
        playback.playSound()
        for (subdivision: Subdivision in subdivisions) {
            subdivision.run()
        }
    }
    private var interonsetInterval: Long = (60000000000.toDouble() / bpm).roundToLong()

    fun stopPulse() {

        scheduledFuture?.cancel(true)
        isRunning = false
    }

    fun startPulse() {
        interonsetInterval = (60000000000.toDouble() / bpm).roundToLong()
        scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(
            runnable,
            0,
            interonsetInterval,
            TimeUnit.NANOSECONDS
        )
        isRunning = true
    }
}