package com.kashif.kmmnewsapp.feature.headlines.presentation.vm

import co.touchlab.skie.configuration.annotations.FlowInterop
import co.touchlab.skie.configuration.annotations.FunctionInterop
import com.kashif.kmmnewsapp.feature.headlines.domain.GetCachedHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.domain.GetTopHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.domain.RefreshHeadlinesUseCase
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesEffect
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesIntent
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesReducer
import com.kashif.kmmnewsapp.feature.headlines.presentation.mvi.HeadlinesState
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class HeadlinesViewModel(

) : ViewModel(), KoinComponent {
    private val getTopHeadlines: GetTopHeadlinesUseCase = get()
    private val refreshHeadlines: RefreshHeadlinesUseCase = get()
    private val getCachedHeadlines: GetCachedHeadlinesUseCase = get()
    private val _state = MutableStateFlow(viewModelScope, HeadlinesState.Loading as HeadlinesState)

    @FlowInterop.Enabled
    val state: StateFlow<HeadlinesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HeadlinesEffect>()

    @FlowInterop.Enabled
    val effect: SharedFlow<HeadlinesEffect> = _effect.asSharedFlow()

    init {
        // Always collect cached headlines for UI updates
        viewModelScope.launch {
            getCachedHeadlines().collect { headlines ->
                _state.value =
                    HeadlinesReducer.reduce(_state.value, HeadlinesIntent.LoadHeadlines, headlines = headlines)
            }
        }
        // Call the API and refresh cache on init
        viewModelScope.launch {
            try {
                refreshHeadlines()
            } catch (e: Exception) {
                _effect.emit(HeadlinesEffect.ShowError(e.message ?: "Failed to load headlines"))
            }
        }
    }

    @FunctionInterop.LegacyName.Disabled
    fun send(intent: HeadlinesIntent) {
        when (intent) {
            is HeadlinesIntent.LoadHeadlines -> {
                _state.value = HeadlinesState.Loading
                viewModelScope.launch {
                    getCachedHeadlines().firstOrNull()?.let { headlines ->
                        _state.value = HeadlinesReducer.reduce(_state.value, intent, headlines = headlines)
                    }
                }
            }

            is HeadlinesIntent.RefreshHeadlines -> {
                _state.value = HeadlinesState.Loading
                viewModelScope.launch {
                    try {
                        refreshHeadlines()
                        getCachedHeadlines().firstOrNull()?.let { headlines ->
                            _state.value = HeadlinesReducer.reduce(_state.value, intent, headlines = headlines)
                        }
                    } catch (e: Exception) {
                        _state.value =
                            HeadlinesReducer.reduce(_state.value, intent, error = e.message ?: "Unknown error")
                        _effect.emit(HeadlinesEffect.ShowError(e.message ?: "Failed to refresh headlines"))
                    }
                }
            }
        }
    }
}
