@file:OptIn(ExperimentalTime::class)

package com.kashif.kmmnewsapp.core.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


interface NetworkConnectivityService {

    val isConnected: StateFlow<Boolean>
    

    val connectivityState: StateFlow<ConnectivityState>
    

    fun startMonitoring()
    

    fun stopMonitoring()
    

    suspend fun checkConnectivity(): Boolean
}

/**
 * Detailed connectivity state with comprehensive information
 * 
 * @param isConnected Whether device has network connectivity
 * @param connectionType Type of network connection (WiFi, Cellular, etc.)
 * @param isMetered Whether connection is metered (affects sync behavior)
 * @param lastConnectedTime Timestamp of last known connection
 * @param accessibilityDescription Human-readable status for screen readers
 */
data class ConnectivityState(
    val isConnected: Boolean = false,
    val connectionType: ConnectionType = ConnectionType.NONE,
    val isMetered: Boolean = false,
    val lastConnectedTime: Long = 0L,
    val accessibilityDescription: String = "Network status unknown"
) {
    /**
     * Whether device should sync data based on connection quality and metering
     */
    val shouldSync: Boolean
        get() = isConnected && (connectionType == ConnectionType.WIFI || !isMetered)
    
    /**
     * Whether device can perform lightweight operations
     */
    val canPerformLightOperations: Boolean
        get() = isConnected
}

/**
 * Network connection types with platform-specific support
 */
enum class ConnectionType {
    NONE,
    WIFI,
    CELLULAR,
    ETHERNET,
    UNKNOWN
}

/**
 * Default implementation providing basic connectivity monitoring
 * Platform-specific implementations will override this
 */
expect class NetworkConnectivityServiceImpl() : NetworkConnectivityService {
    override val isConnected: StateFlow<Boolean>
    override val connectivityState: StateFlow<ConnectivityState>
    override fun startMonitoring()
    override fun stopMonitoring()
    override suspend fun checkConnectivity(): Boolean
}

/**
 * Common base implementation with shared logic
 */
abstract class BaseNetworkConnectivityService : NetworkConnectivityService {
    protected val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    protected val _connectivityState = MutableStateFlow(ConnectivityState())
    override val connectivityState: StateFlow<ConnectivityState> = _connectivityState.asStateFlow()
    
    /**
     * Update connectivity state with proper accessibility descriptions
     */
    protected fun updateConnectivityState(
        isConnected: Boolean,
        connectionType: ConnectionType = ConnectionType.UNKNOWN,
        isMetered: Boolean = false
    ) {
        val accessibilityDescription = when {
            !isConnected -> "Device is offline. Showing cached content."
            connectionType == ConnectionType.WIFI -> "Connected to WiFi. All features available."
            connectionType == ConnectionType.CELLULAR && isMetered -> "Connected via cellular data. Limited sync enabled."
            connectionType == ConnectionType.CELLULAR -> "Connected via cellular data. All features available."
            else -> "Connected to network. All features available."
        }
        
        val newState = ConnectivityState(
            isConnected = isConnected,
            connectionType = connectionType,
            isMetered = isMetered,
            lastConnectedTime = if (isConnected) Clock.System.now().toEpochMilliseconds() else _connectivityState.value.lastConnectedTime,
            accessibilityDescription = accessibilityDescription
        )
        
        _isConnected.value = isConnected
        _connectivityState.value = newState
    }
} 