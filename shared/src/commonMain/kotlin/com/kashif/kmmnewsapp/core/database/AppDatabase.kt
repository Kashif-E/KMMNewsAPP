@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.kashif.kmmnewsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.ConstructedBy
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import kotlin.time.Clock


@Database(
    entities = [HeadlineEntity::class, CounterEntity::class],
    version = 2,
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
