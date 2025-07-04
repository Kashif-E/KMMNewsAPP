package com.kashif.kmmnewsapp

// SKIE optimizations for maximum Swift ergonomics
import co.touchlab.skie.configuration.annotations.FlowInterop
import co.touchlab.skie.configuration.annotations.FunctionInterop
import com.rickclephas.kmp.observableviewmodel.MutableStateFlow
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import com.kashif.kmmnewsapp.core.database.DatabaseProvider
import com.kashif.kmmnewsapp.core.database.CounterEntity
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Sample Observable ViewModel with SKIE optimizations
 *
 * This ViewModel is optimized for Swift usage with:
 * - Enhanced Flow interoperability (Swift AsyncSequence)
 * - Modern function naming conventions
 * - Optimal StateFlow handling
 */
open class SampleObservableViewModel() : ViewModel(), KoinComponent {
    private val databaseProvider: DatabaseProvider = get()

    private val _counter = MutableStateFlow(viewModelScope, 0)

    /**
     * Counter state optimized for Swift AsyncSequence usage
     * Will be accessible as AsyncSequence in Swift
     */
    @FlowInterop.Enabled
    val counter: StateFlow<Int> = _counter.asStateFlow()

    /**
     * Derived state that doubles the counter value
     * Automatically converts to Swift AsyncSequence
     */
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
        // Observe changes and persist
        viewModelScope.launch {
            counter.collect { value ->
                val db = databaseProvider.getDatabase()
                db.counterDao().setCounter(CounterEntity(value = value))
            }
        }
    }

    /**
     * Increment function with Swift-optimized naming
     * Will have clean, idiomatic Swift method name
     */
    @FunctionInterop.LegacyName.Disabled
    fun increment() {
        _counter.value = _counter.value + 1
    }

    /**
     * Decrement function for completeness
     * Also optimized for Swift naming conventions
     */
    @FunctionInterop.LegacyName.Disabled
    fun decrement() {
        _counter.value = _counter.value - 1
    }

    /**
     * Reset function with default parameter
     * Demonstrates SKIE's default argument handling
     */
    @FunctionInterop.LegacyName.Disabled
    fun reset(to: Int = 0) {
        _counter.value = to
    }
}
