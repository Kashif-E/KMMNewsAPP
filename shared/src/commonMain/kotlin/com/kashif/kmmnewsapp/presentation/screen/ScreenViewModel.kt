package com.kashif.kmmnewsapp.presentation.screen

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ScreenViewModel: ViewModel() {

    var state = MutableStateFlow(ScreenState())

    fun getSharedMessage()= state.value.message
}

