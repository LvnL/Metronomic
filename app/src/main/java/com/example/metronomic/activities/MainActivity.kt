package com.example.metronomic.activities

import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import com.example.metronomic.*
import com.example.metronomic.timing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val eventBus = EventBus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(CONTENT_TYPE_SONIFICATION)
            .setUsage(USAGE_MEDIA)
            .build()
        val soundPool: SoundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build()
        val soundID: Int = soundPool.load(this, R.raw.sample_click, 1)

        val pulse = Pulse(eventBus, soundPool, soundID)

        val pulseButton: ImageButton = findViewById(R.id.pulseButton)
        pulseButton.setOnClickListener {
            if (pulse.pulseIsRunning) {
                pulse.stopPulse()
            } else {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        pulse.startPulse()
                    }
                }
            }
        }

        val bpmTextBox: EditText = findViewById(R.id.bpmTextBox)
        bpmTextBox.onSubmit {
            pulse.bpm = bpmTextBox.text.toString().toInt()
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