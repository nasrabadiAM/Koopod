package com.nasabadiam.koopod.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


fun <T> Fragment.collectAsState(flow: Flow<T>, func: (t: T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.collect {
            func.invoke(it)
        }
    }
}

fun Fragment.showPopupMessage(message: String) {
    Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        message,
        Snackbar.LENGTH_LONG
    ).show()
}