package com.example.metronomic.activities

import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.metronomic.*
import com.example.metronomic.timing.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(CONTENT_TYPE_SONIFICATION)
            .setUsage(USAGE_MEDIA)
            .build()
        val soundPool: SoundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(10)
            .build()
        val soundID: Int = soundPool.load(this, R.raw.sample_click, 1)

        val pulse = Pulse(soundPool, soundID)

        val bpmTextBox: EditText = findViewById(R.id.bpmTextBox)
        bpmTextBox.onSubmit {
            var pulseWasRunning = false
            if (pulse.isRunning) {
                pulse.stopPulse()
                pulseWasRunning = true
            }
            pulse.bpm = bpmTextBox.text.toString().toInt()
            if (pulseWasRunning) {
                pulse.startPulse()
            }
        }

        val pulseButton: ImageButton = findViewById(R.id.pulseButton)
        pulseButton.setOnClickListener {
            if (pulse.isRunning) {
                pulse.stopPulse()
            } else {
                pulse.startPulse()
            }
        }

        val subdivisionButton: Button = findViewById(R.id.subdivisionButton)
        var subdivision: Subdivision? = null
        subdivisionButton.setOnClickListener {
            subdivision = if (subdivision == null) {
                Subdivision(pulse, subdivisionButton.text.toString().toInt(), soundPool, soundID)
            } else {
                subdivision?.disable()
                null
            }
        }
    }

    private fun EditText.onSubmit(func: () -> Unit) {
        setOnEditorActionListener { _, actionID, _ ->
            if (actionID == EditorInfo.IME_ACTION_DONE) {
                func()
            }

            true
        }
    }
}