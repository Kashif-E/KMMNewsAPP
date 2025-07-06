package com.kashif.kmmnewsapp.android

import android.app.Application
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.kashif.kmmnewsapp.core.database.appContext
import com.kashif.kmmnewsapp.core.di.initKoin
import com.kashif.kmmnewsapp.core.network.NetworkConnectivityServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainApplication : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        
        // Initialize Coil
        initializeCoil()
        
        initKoin(baseUrl = "https://newsapi.org/v2/", enableNetworkLogs = true) {
            androidContext(this@MainApplication)
        }

        val networkService: NetworkConnectivityServiceImpl by inject()
        networkService.initialize(this)
        networkService.startMonitoring()

        android.util.Log.i("NetworkService", "Initial connectivity: ${networkService.connectivityState.value}")
    }
    
    private fun initializeCoil() {
        val imageLoader = ImageLoader.Builder(this)
            .build()
            
        SingletonImageLoader.setSafe { imageLoader }
        android.util.Log.i("Coil", "ImageLoader initialized successfully")
    }
}
