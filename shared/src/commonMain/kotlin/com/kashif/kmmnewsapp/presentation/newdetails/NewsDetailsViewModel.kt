package com.kashif.kmmnewsapp.presentation.newdetails

import dev.icerock.moko.mvvm.viewmodel.ViewModel

class NewsDetailsViewModel : ViewModel() {

    fun onIntent(intent: NewsDetailsScreenEvent){
        when(intent){
            is NewsDetailsScreenEvent.SaveForLater -> {

            }
        }
    }
}