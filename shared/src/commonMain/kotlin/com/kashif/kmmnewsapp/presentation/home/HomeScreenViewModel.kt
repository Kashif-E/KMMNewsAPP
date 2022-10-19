package com.kashif.kmmnewsapp.presentation.home

import com.kashif.kmmnewsapp.domain.usecase.home.GetHeadlinesUseCase
import com.kashif.kmmnewsapp.domain.util.Result
import com.kashif.kmmnewsapp.domain.util.asResult
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val getHeadlinesUseCase: GetHeadlinesUseCase) : ViewModel() {

    private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Idle)
    var state = _state.asStateFlow()
    private var page: Int = 1
    fun onIntent(intent: HomeScreenSideEvent) {

        when (intent) {
            is HomeScreenSideEvent.GetHeadlines -> {
                getHeadlines()
            }
        }
    }

    private fun getHeadlines() {

        viewModelScope.launch {

            getHeadlinesUseCase.invoke(page = page).asResult().collectLatest { result ->

                when (result) {
                    is Result.Error -> {
                        if (page == 1) {
                            _state.update {
                                HomeScreenState.Error(result.exception.message)
                            }
                        }
                    }
                    Result.Idle -> {
                        if (page == 1) {
                            _state.update {
                                HomeScreenState.Idle
                            }
                        }

                    }
                    Result.Loading -> {
                        if (page == 1) {
                            _state.update {
                                HomeScreenState.Loading
                            }
                        }
                    }
                    is Result.Success -> {
                        if (page == 1) {
                            _state.update {
                                HomeScreenState.Success(result.data)
                            }
                        } else {
                            _state.update {
                                (it as HomeScreenState.Success).copy(it.headlines + result.data)
                            }
                        }
                    }
                }

            }

        }

    }
}

