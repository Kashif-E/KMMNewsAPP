package com.kashif.kmmnewsapp

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SampleNativeCoroutines {
    @NativeCoroutines
    suspend fun getGreeting(): String {
        delay(500)
        return "Hello from KMP!"
    }

    @NativeCoroutinesState
    val counter: StateFlow<Int> = MutableStateFlow(0)
}
