package com.kashif.kmmnewsapp.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


actual class NetworkConnectivityServiceImpl : NetworkConnectivityService {
    
    private var context: Context? = null
    
    private val _isConnected = MutableStateFlow(false)
    actual override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    private val _connectivityState = MutableStateFlow(
        ConnectivityState(
            isConnected = false,
            connectionType = ConnectionType.NONE,
            isMetered = false,
            accessibilityDescription = "Not connected"
        )
    )
    actual override val connectivityState: StateFlow<ConnectivityState> = _connectivityState.asStateFlow()
    fun initialize(context: Context) {
        this.context = context
    }

    private var connectivityManager: ConnectivityManager? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var isMonitoring = false


    override actual fun startMonitoring() {
        val ctx = context ?: return
        connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (isMonitoring) return
        

        updateNetworkState()
        

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                updateNetworkState()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                updateNetworkState()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                updateNetworkState()
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback?.let { callback ->
            connectivityManager?.registerNetworkCallback(networkRequest, callback)
            isMonitoring = true
        }
    }


    actual override fun stopMonitoring() {
        if (!isMonitoring) return
        
        networkCallback?.let { callback ->
            try {
                connectivityManager?.unregisterNetworkCallback(callback)
            } catch (e: IllegalArgumentException) {

            }
        }
        networkCallback = null
        isMonitoring = false
    }


    actual override suspend fun checkConnectivity(): Boolean {
        return try {
            val manager = connectivityManager ?: return false
            val activeNetwork = manager.activeNetwork
            val networkCapabilities = manager.getNetworkCapabilities(activeNetwork)
            
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: Exception) {
            false
        }
    }


    private fun updateNetworkState() {
        try {
            val manager = connectivityManager ?: return
            val activeNetwork = manager.activeNetwork
            val networkCapabilities = manager.getNetworkCapabilities(activeNetwork)
            
            if (networkCapabilities == null) {
                updateConnectivityState(false, ConnectionType.NONE, false)
                return
            }

            val isConnected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                             networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            if (!isConnected) {
                updateConnectivityState(false, ConnectionType.NONE, false)
                return
            }


            val connectionType = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.CELLULAR
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
                else -> ConnectionType.UNKNOWN
            }


            val isMetered = !networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)

            updateConnectivityState(isConnected, connectionType, isMetered)
            
        } catch (e: Exception) {

            val manager = connectivityManager ?: return
            val isConnected = manager.activeNetworkInfo?.isConnected == true
            updateConnectivityState(isConnected, ConnectionType.UNKNOWN, false)
        }
    }
    

    private fun updateConnectivityState(
        isConnected: Boolean, 
        connectionType: ConnectionType, 
        isMetered: Boolean
    ) {
        _isConnected.value = isConnected
        
        val shouldSync = isConnected && (!isMetered || connectionType == ConnectionType.WIFI)
        val accessibilityDescription = generateAccessibilityDescription(isConnected, connectionType, isMetered)
        
        _connectivityState.value = ConnectivityState(
            isConnected = isConnected,
            connectionType = connectionType,
            isMetered = isMetered,
            accessibilityDescription = accessibilityDescription
        )
    }
    

    private fun generateAccessibilityDescription(
        isConnected: Boolean,
        connectionType: ConnectionType,
        isMetered: Boolean
    ): String {
        return when {
            !isConnected -> "No internet connection"
            connectionType == ConnectionType.WIFI -> "Connected to WiFi${if (isMetered) " (metered)" else ""}"
            connectionType == ConnectionType.CELLULAR -> "Connected to cellular data${if (isMetered) " (metered)" else ""}"
            connectionType == ConnectionType.ETHERNET -> "Connected to wired internet"
            else -> "Connected to internet"
        }
    }
} 