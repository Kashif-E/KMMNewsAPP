package com.kashif.kmmnewsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.ConstructedBy
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [HeadlineEntity::class, CounterEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(AppDatabaseCtor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun headlinesDao(): HeadlinesDao
    abstract fun counterDao(): CounterDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseCtor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
