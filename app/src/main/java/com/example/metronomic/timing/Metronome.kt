package com.example.metronomic.timing

import android.media.AudioTrack
import com.example.metronomic.audio.Generator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class Metronome(private val audioTrack: AudioTrack) {
    var bpm: Int = 124
    var isRunning: Boolean = false

    private val executorService: ExecutorService = Executors.newFixedThreadPool(1)
    private var pulseFrequency: Float = 1975.53F
    private var pulseLength: Float = 0.05F
    private val generator: Generator = Generator(audioTrack.sampleRate)

    fun start() {
        isRunning = true
        executorService.submit {
            val interonsetInterval: Float = 60F / bpm
            val pulseSample: DoubleArray = generator.generateSineWave(pulseFrequency, pulseLength)

            val initialFullSample = generateFullSample(audioTrack.sampleRate, interonsetInterval, pulseSample)
            audioTrack.write(initialFullSample, 0, initialFullSample.size, AudioTrack.WRITE_BLOCKING)
            audioTrack.play()

            while (isRunning) {
                val queuedFullSample = generateFullSample(audioTrack.sampleRate, interonsetInterval, pulseSample)
                audioTrack.write(queuedFullSample, 0, queuedFullSample.size, AudioTrack.WRITE_BLOCKING)
            }
        }
    }

    fun stop() {
        isRunning = false
        audioTrack.stop()
    }

    private fun generateFullSample(sampleRate: Int, interonsetInterval: Float, pulseSample: DoubleArray): FloatArray {
        val fullSample = FloatArray((sampleRate * interonsetInterval).roundToInt())
        for (i in fullSample.indices) {
            if (i < pulseSample.size) {
                fullSample[i] = pulseSample[i].toFloat()
            } else {
                fullSample[i] = 0F
            }
        }

        return fullSample
    }
}