package com.nasabadiam.koopod.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.openKeyboard() {
    post {
        if (requestFocus()) {
            try {
                val imm = context.getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as? InputMethodManager
                imm?.let {
                    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
                }
            } catch (e: Exception) {
                Log.e("Koopod Keyboard state", "Keyboard has a problem while opening:", e)
            }
        }
    }
}

fun View.hideKeyboard() {
    post {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}