package com.example.metronomic.activities

import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import com.example.metronomic.*
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    private var metronomeIsRunning: Boolean? = null
    private var timer: Timer? = null
    private var bpm: Int = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        metronomeIsRunning = false

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(CONTENT_TYPE_SONIFICATION)
            .setUsage(USAGE_MEDIA)
            .build()
        val soundPool: SoundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()
        val soundID: Int = soundPool.load(this, R.raw.sample_click, 1)
        soundPool.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f)

        val bpmTextBox: EditText = findViewById(R.id.bpmTextBox)
        bpmTextBox.setOnFocusChangeListener { _, hasFocus ->
            run {
                if (!hasFocus) {
                    if (metronomeIsRunning == true) {
                        stopMetronome()
                        startMetronome(soundPool, soundID)
                    }
                }
            }
        }

        val metronomePlayBackButton: ImageButton = findViewById(R.id.metronomePlaybackButton)
        metronomePlayBackButton.setOnClickListener {
            if (metronomeIsRunning == true) {
                stopMetronome()
            } else {
                startMetronome(soundPool, soundID)
            }
        }
    }

    private fun startMetronome(soundPool: SoundPool, soundID: Int) {
        timer = Timer()
        val timerTask: TimerTask = timerTask {
            soundPool.play(soundID, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        val bpmTextBox: EditText = findViewById(R.id.bpmTextBox)
        bpm = Integer.parseInt(bpmTextBox.text.toString())
        timer?.scheduleAtFixedRate(timerTask, 0, (60000.toDouble() / bpm).roundToLong())
        metronomeIsRunning = true
    }

    private fun stopMetronome() {
        timer?.cancel()
        metronomeIsRunning = false
    }
}