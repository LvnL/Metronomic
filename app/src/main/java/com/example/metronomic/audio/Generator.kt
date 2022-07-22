package com.example.metronomic.audio

import kotlin.math.roundToInt
import kotlin.math.sin

class Generator(private val sampleRate: Int) {
    fun generateSineWave(frequency: Float, durationInSeconds: Float): DoubleArray {
        val durationInSamples: Int = (sampleRate * durationInSeconds).roundToInt()
        val sample = DoubleArray(durationInSamples)

        for (i in sample.indices) {
            sample[i] = sin(2 * Math.PI * i / (sampleRate / frequency)) * ((durationInSamples - i) / durationInSamples)
        }

        return sample
    }
}