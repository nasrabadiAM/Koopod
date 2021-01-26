package com.nasabadiam.koopod.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Fragment.collectAsState(flow: StateFlow<T>, func: (t: T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        flow.collect {
            func.invoke(it)
        }
    }
}