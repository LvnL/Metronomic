package com.example.metronomic.activities

import android.media.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.example.metronomic.*
import com.example.metronomic.timing.Metronome

class MainActivity : AppCompatActivity() {
    private val audioTrack: AudioTrack

    init {
        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()
        val audioFormat: AudioFormat = AudioFormat.Builder()
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
            .setSampleRate(44100)
            .build()
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(audioAttributes)
            .setAudioFormat(audioFormat)
            .setBufferSizeInBytes(AudioTrack.getMinBufferSize(
                audioFormat.sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                audioFormat.encoding))
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val metronome = Metronome(audioTrack)

        val bpmTextBox: EditText = findViewById(R.id.bpmTextBox)
        bpmTextBox.onSubmit {
            val enteredBPM: Int = bpmTextBox.text.toString().toInt()
            if (enteredBPM != metronome.bpm) {
                val metronomeWasRunning: Boolean = metronome.isRunning
                if(metronomeWasRunning) {
                    metronome.stop()
                }
                metronome.bpm = enteredBPM
                if (metronomeWasRunning) {
                    metronome.start()
                }
            }
        }

        val pulseButton: ImageButton = findViewById(R.id.pulseButton)
        pulseButton.setOnClickListener {
            if (metronome.isRunning) {
                metronome.stop()
            } else {
                metronome.start()
            }
        }

        val subdivisionButton: Button = findViewById(R.id.subdivisionButton)
        subdivisionButton.setOnClickListener {

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