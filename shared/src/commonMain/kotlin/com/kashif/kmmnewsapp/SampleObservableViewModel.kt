package com.kashif.kmmnewsapp


import co.touchlab.skie.configuration.annotations.FlowInterop
import co.touchlab.skie.configuration.annotations.FunctionInterop
import com.kashif.kmmnewsapp.core.database.CounterEntity
import com.kashif.kmmnewsapp.core.database.DatabaseProvider
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


open class SampleObservableViewModel() : ViewModel(), KoinComponent {
    private val databaseProvider: DatabaseProvider = get()

    private val _counter = MutableStateFlow(viewModelScope, 0)


    @FlowInterop.Enabled
    val counter: StateFlow<Int> = _counter.asStateFlow()


    @FlowInterop.Enabled
    val doubled: StateFlow<Int> = counter
        .map { it * 2 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0)

    init {
        viewModelScope.launch {
            val db = databaseProvider.getDatabase()
            val value = db.counterDao().getCounter() ?: 0
            _counter.value = value
        }

        viewModelScope.launch {
            counter.collect { value ->
                val db = databaseProvider.getDatabase()
                db.counterDao().setCounter(CounterEntity(value = value))
            }
        }
    }


    @FunctionInterop.LegacyName.Disabled
    fun increment() {
        _counter.value = _counter.value + 1
    }


    @FunctionInterop.LegacyName.Disabled
    fun decrement() {
        _counter.value = _counter.value - 1
    }


    @FunctionInterop.LegacyName.Disabled
    fun reset(to: Int = 0) {
        _counter.value = to
    }
}
