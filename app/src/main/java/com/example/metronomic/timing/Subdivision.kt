package com.example.metronomic.timing

import android.media.SoundPool
import com.example.metronomic.audio.Playback
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

class Subdivision(private val pulse: Pulse, private val noteQuantity: Int, soundPool: SoundPool, soundID: Int) : TimeEmitter() {
    init {
        pulse.subdivisions.add(this)
    }

    override val playback = Playback(soundPool, soundID)
    override val scheduledThreadPoolExecutor = pulse.scheduledThreadPoolExecutor
    override val type = Type.SUBDIVISION

    private var interonsetInterval =
        (60000000000.toDouble() / pulse.bpm / noteQuantity).roundToLong()
    private val subdivisionPulses: ArrayList<ScheduledFuture<*>?> = ArrayList()
    private var runnable: Runnable = Runnable {
        playback.playSound()
    }

    fun disable() {
        for (scheduledFuture: ScheduledFuture<*>? in subdivisionPulses) {
            scheduledFuture?.cancel(true)
        }
        pulse.subdivisions.remove(this)
    }

    fun run() {
        interonsetInterval = (60000000000.toDouble() / pulse.bpm / noteQuantity).roundToLong()
        for (i in 1 until noteQuantity) {
            println()
            subdivisionPulses.add(
                scheduledThreadPoolExecutor.schedule(
                    runnable,
                    i * interonsetInterval,
                    TimeUnit.NANOSECONDS
                )
            )
        }
    }
}