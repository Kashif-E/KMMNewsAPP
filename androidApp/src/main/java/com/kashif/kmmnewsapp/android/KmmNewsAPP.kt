package com.kashif.kmmnewsapp.android

import android.app.Application
import com.kashif.kmmnewsapp.core.di.initKoin

class KmmNewsAPP : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(baseUrl = "https://newsapi.org/v2/") {

        }
    }
}
