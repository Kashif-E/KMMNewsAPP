package com.kashif.kmmnewsapp

import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.stateIn
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

open class SampleObservableViewModel : ViewModel() {
    private val _counter = MutableStateFlow(viewModelScope, 0)
    @NativeCoroutinesState
    val counter: StateFlow<Int> = _counter.asStateFlow()

    @NativeCoroutinesState
    val doubled: StateFlow<Int> = counter
        .map { it * 2 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    fun increment() {
        _counter.value = _counter.value + 1
    }
}
