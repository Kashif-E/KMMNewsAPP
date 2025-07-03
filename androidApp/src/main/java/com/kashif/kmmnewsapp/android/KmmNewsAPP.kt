package com.kashif.kmmnewsapp.android

import android.app.Application
import com.kashif.kmmnewsapp.domain.di.initKoin

class KmmNewsAPP : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(baseUrl = "https://newsapi.org/v2/") {
            // You can add additional Koin modules or configurations here if needed
        }
    }
}
