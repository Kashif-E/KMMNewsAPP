package com.kashif.kmmnewsapp.core.di

import com.kashif.kmmnewsapp.core.database.databaseModule
import com.kashif.kmmnewsapp.core.database.getDatabaseBuilder
import com.kashif.kmmnewsapp.core.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val coreModule: Module = module {
    includes(databaseModule {
        getRoomDatabase(getDatabaseBuilder())
    })
    // TODO: Add other core singletons (e.g., Ktor client)
}
