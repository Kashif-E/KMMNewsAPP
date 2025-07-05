package com.kashif.kmmnewsapp.android

import android.app.Application
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
        initKoin(baseUrl = "https://newsapi.org/v2/", enableNetworkLogs = true) {
            androidContext(this@MainApplication)
        }

        val networkService: NetworkConnectivityServiceImpl by inject()
        networkService.initialize(this)
        networkService.startMonitoring()

        android.util.Log.i("NetworkService", "Initial connectivity: ${networkService.connectivityState.value}")
    }
}
