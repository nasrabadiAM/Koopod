package com.nasabadiam.koopod.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


fun <T> AppCompatActivity.collectAsState(flow: Flow<T>, func: (t: T) -> Unit) {
    lifecycleScope.launch {
        flow.collect {
            func.invoke(it)
        }
    }
}