package com.kashif.kmmnewsapp.presentation.home

import com.kashif.kmmnewsapp.domain.usecase.GetHeadlinesUseCase
import com.kashif.kmmnewsapp.domain.util.DataState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val getHeadlinesUseCase: GetHeadlinesUseCase) : ViewModel() {

    var state = MutableStateFlow(HomeScreenState())


    fun onIntent(intent: HomeScreenSideEffects) {

        when (intent) {
            is HomeScreenSideEffects.GetHeadlines -> {
                getHeadlines()
            }
        }
    }

    private fun getHeadlines() {

        viewModelScope.launch {


            getHeadlinesUseCase.invoke(page = state.value.page).collectLatest { dataState ->

                when (dataState) {
                    is DataState.Success -> {

                        state.emit(
                            state.value.copy(
                                isLoading = false,
                                isSuccess = true,
                                headlines = dataState.data ?: emptyList()
                            )
                        )

                    }
                    is DataState.Error -> {

                        state.emit(
                            state.value.copy(
                                isLoading = false,
                                isSuccess = false,
                                error = Error(true, dataState.error.message),
                            )
                        )


                    }
                    else -> {

                        state.emit(
                            state.value.copy(
                                isLoading = false,
                                isSuccess = false,
                                error = Error(true, dataState.error.message),
                            )
                        )

                    }
                }

            }

        }

    }
}

