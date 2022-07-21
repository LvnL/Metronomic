package com.example.metronomic.timing

import com.example.metronomic.audio.Playback
import java.util.concurrent.ScheduledThreadPoolExecutor

abstract class TimeEmitter {
    abstract val playback: Playback
    abstract val scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor
    abstract val type: Type
}

enum class Type {
    PULSE,
    SUBDIVISION
}