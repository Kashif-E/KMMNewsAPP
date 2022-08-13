package com.kashif.kmmnewsapp.android

import android.app.Application
import com.kashif.kmmnewsapp.domain.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


class KmmNewsAPP : Application(){

    override fun onCreate() {
        super.onCreate()

        initKoin(baseUrl = "Your Base Url", enableNetworkLogs = BuildConfig.DEBUG) {
            androidContext(this@KmmNewsAPP)
            // androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.INFO)
            modules(
                listOf(module {
                    /**
                     * android specific modules
                     */
                })
            )
        }
    }
}