package com.kashif.kmmnewsapp.core.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import androidx.room.RoomDatabase


class DatabaseProvider {
    private val mutex = Mutex()
    private var database: AppDatabase? = null

    suspend fun getDatabase(): AppDatabase = mutex.withLock(Dispatchers.IO) {
        if (database == null) {
            database = getDatabaseBuilder()
                .build()
        }
        database!!
    }

    suspend fun clearDatabase() = mutex.withLock(Dispatchers.IO) {
        database?.headlinesDao()?.clearAll()
    }

    private var instance: DatabaseProvider? = null
    private val instanceMutex = Mutex()

    suspend fun getInstance(): DatabaseProvider = instanceMutex.withLock(Dispatchers.IO) {
        if (instance == null) {
            instance = DatabaseProvider()
        }
        instance!!
    }
}
