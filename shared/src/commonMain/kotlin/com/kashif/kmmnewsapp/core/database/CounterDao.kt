package com.kashif.kmmnewsapp.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CounterDao {
    @Query("SELECT value FROM counter WHERE id = 0 LIMIT 1")
    fun observeCounter(): Flow<Int?>

    @Query("SELECT value FROM counter WHERE id = 0 LIMIT 1")
    suspend fun getCounter(): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setCounter(counter: CounterEntity)
} 