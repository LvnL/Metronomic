package com.example.metronomic.timing

import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.roundToLong

private var timer: Timer? = null
private var metronomeIsRunning: Boolean = false
private var bpm: Int = 124

fun changeBPM(newBPM: Int) {
    if (metronomeIsRunning)
        stopMetronome()
    bpm = newBPM
    startMetronome()
}

fun stopMetronome() {
    timer?.cancel()
    timer = Timer()
    metronomeIsRunning = false
}

fun startMetronome() {
    timer = Timer()
    timer?.scheduleAtFixedRate(getTimerTask(), 0, (60000.toDouble() / bpm).roundToLong())
    metronomeIsRunning = true
}

private fun getTimerTask(): TimerTask {
    return timerTask {

    }
}