package com.kashif.kmmnewsapp.android

import android.app.Application
import com.kashif.kmmnewsapp.core.database.appContext
import com.kashif.kmmnewsapp.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        initKoin(baseUrl = "https://newsapi.org/v2/") {
            androidContext(this@MainApplication)

        }
    }
}
