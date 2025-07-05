package com.kashif.kmmnewsapp.core.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.Foundation.NSURLSession

import platform.Foundation.NSURL
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURLRequestReloadIgnoringLocalCacheData
import platform.Foundation.dataTaskWithRequest
import platform.Network.*
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.resume
import kotlin.experimental.ExperimentalNativeApi


@OptIn(ExperimentalNativeApi::class)
actual class NetworkConnectivityServiceImpl : BaseNetworkConnectivityService() {
    

    private var pathMonitor: nw_path_monitor_t? = null
    private var monitorQueue: dispatch_queue_t? = null
    

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val stateMutex = Mutex()
    

    private var isCurrentlyMonitoring = false
    

    private val connectivityTestUrls = listOf(
        "https://www.apple.com",
        "https://www.google.com",
        "https://1.1.1.1"
    )
    private val httpTimeoutMs = 5000L
    
    actual override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    actual override val connectivityState: StateFlow<ConnectivityState> = _connectivityState.asStateFlow()

    
    actual override fun startMonitoring() {
        serviceScope.launch {
            stateMutex.withLock {
                if (isCurrentlyMonitoring) {
                    println("NetworkConnectivityService: Monitoring already active, skipping start")
                    return@withLock
                }
                
                try {
                    initializePathMonitor()
                    isCurrentlyMonitoring = true
                    println("NetworkConnectivityService: Successfully started network monitoring")
                } catch (e: Exception) {
                    println("NetworkConnectivityService: Failed to start monitoring: ${e.message}")
                    handleMonitoringError(e)
                }
            }
        }
    }

    
    actual override fun stopMonitoring() {
        serviceScope.launch {
            stateMutex.withLock {
                if (!isCurrentlyMonitoring) {
                    println("NetworkConnectivityService: Monitoring not active, skipping stop")
                    return@withLock
                }
                
                try {
                    cleanupPathMonitor()
                    isCurrentlyMonitoring = false
                    println("NetworkConnectivityService: Successfully stopped network monitoring")
                } catch (e: Exception) {
                    println("NetworkConnectivityService: Error during monitoring cleanup: ${e.message}")
                }
            }
        }
    }

    
    actual override suspend fun checkConnectivity(): Boolean {
        return withTimeoutOrNull(httpTimeoutMs) {
            try {
                performHttpConnectivityCheck()
            } catch (e: Exception) {
                println("NetworkConnectivityService: HTTP connectivity check failed: ${e.message}")
                false
            }
        } ?: false
    }

    
    private fun initializePathMonitor() {
        try {

            monitorQueue = dispatch_queue_create(
                "com.kashif.kmmnewsapp.network.monitor", 
                null
            )
            

            pathMonitor = nw_path_monitor_create()
            
            val monitor = pathMonitor
            val queue = monitorQueue
            
            if (monitor == null || queue == null) {
                throw IllegalStateException("Failed to create path monitor or queue")
            }
            

            nw_path_monitor_set_update_handler(monitor) { path ->
                handlePathUpdate(path)
            }
            

            nw_path_monitor_set_queue(monitor, queue)
            

            nw_path_monitor_start(monitor)
            
            println("NetworkConnectivityService: nw_path_monitor initialized successfully")
            
        } catch (e: Exception) {
            println("NetworkConnectivityService: Failed to initialize nw_path_monitor: ${e.message}")
            throw e
        }
    }

    
    private fun handlePathUpdate(path: nw_path_t?) {
        serviceScope.launch {
            try {
                if (path == null) {
                    updateConnectivityState(false, ConnectionType.NONE, false)
                    return@launch
                }
                

                val pathStatus = nw_path_get_status(path)
                val isConnected = pathStatus == nw_path_status_satisfied
                
                if (!isConnected) {
                    updateConnectivityState(false, ConnectionType.NONE, false)
                    return@launch
                }
                

                val connectionType = determineConnectionType(path)
                

                val isMetered = nw_path_is_expensive(path)
                

                updateConnectivityState(isConnected, connectionType, isMetered)
                
            } catch (e: Exception) {
                println("NetworkConnectivityService: Error handling path update: ${e.message}")

                updateConnectivityState(true, ConnectionType.UNKNOWN, false)
            }
        }
    }

    
    private fun determineConnectionType(path: nw_path_t): ConnectionType {
        return try {
            when {
                nw_path_uses_interface_type(path, nw_interface_type_wifi) -> ConnectionType.WIFI
                nw_path_uses_interface_type(path, nw_interface_type_cellular) -> ConnectionType.CELLULAR
                nw_path_uses_interface_type(path, nw_interface_type_wired) -> ConnectionType.ETHERNET
                else -> ConnectionType.UNKNOWN
            }
        } catch (e: Exception) {
            println("NetworkConnectivityService: Error determining connection type: ${e.message}")
            ConnectionType.UNKNOWN
        }
    }

    
    private suspend fun performHttpConnectivityCheck(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            var completed = false
            
            connectivityTestUrls.forEachIndexed { index, url ->
                if (completed) return@forEachIndexed
                
                try {
                    val nsUrl = NSURL.URLWithString(url) ?: return@forEachIndexed
                    val mutableRequest = NSMutableURLRequest.requestWithURL(nsUrl).apply {
                        setTimeoutInterval(httpTimeoutMs / 1000.0)
                        setCachePolicy(NSURLRequestReloadIgnoringLocalCacheData)
                    }
                    
                    val session = NSURLSession.sharedSession
                    val task = session.dataTaskWithRequest(mutableRequest) { data, response, error ->
                        if (completed) return@dataTaskWithRequest
                        
                        if (error == null && response != null) {
                            completed = true
                            continuation.resume(true)
                        } else if (index == connectivityTestUrls.size - 1) {

                            completed = true
                            continuation.resume(false)
                        }
                    }
                    
                    task.resume()
                    
                } catch (_: Exception) {
                    if (index == connectivityTestUrls.size - 1 && !completed) {
                        completed = true
                        continuation.resume(false)
                    }
                }
            }
            

            continuation.invokeOnCancellation {
                completed = true
            }
        }
    }

    
    private fun cleanupPathMonitor() {
        try {
            pathMonitor?.let { monitor ->
                nw_path_monitor_cancel(monitor)
            }
            pathMonitor = null
            monitorQueue = null
            
            println("NetworkConnectivityService: nw_path_monitor cleanup completed")
            
        } catch (e: Exception) {
            println("NetworkConnectivityService: Error during nw_path_monitor cleanup: ${e.message}")
        }
    }

    
    private fun handleMonitoringError(error: Exception) {
        println("NetworkConnectivityService: Monitoring error occurred: ${error.message}")
        

        updateConnectivityState(false, ConnectionType.NONE, false)
        

        try {
            cleanupPathMonitor()
        } catch (cleanupError: Exception) {
            println("NetworkConnectivityService: Additional error during cleanup: ${cleanupError.message}")
        }
        
        isCurrentlyMonitoring = false
    }
}
