package com.example.metronomic.events

import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.onSubmit(func: () -> Unit) {
    setOnEditorActionListener { _, actionID, _ ->
        if (actionID == EditorInfo.IME_ACTION_DONE) {
            func()
        }
        true
    }
}