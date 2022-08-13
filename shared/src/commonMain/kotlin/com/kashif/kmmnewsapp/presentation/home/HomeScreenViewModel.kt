package com.kashif.kmmnewsapp.presentation.home

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class HomeScreenViewModel: ViewModel() {

    var state = MutableStateFlow(HomeScreenState())

    fun getSharedMessage()= state.value.message
}

