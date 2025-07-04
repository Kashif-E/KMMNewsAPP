package com.kashif.kmmnewsapp.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadlinesDao {
    @Query("SELECT * FROM headlines ORDER BY publishedAt DESC")
    fun getAllHeadlines(): Flow<List<HeadlineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeadlines(headlines: List<HeadlineEntity>)

    @Query("DELETE FROM headlines")
    suspend fun clearAll()

    @Delete
    suspend fun deleteHeadline(headline: HeadlineEntity)
}
