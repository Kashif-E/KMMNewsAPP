package com.kashif.kmmnewsapp.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter")
data class CounterEntity(
    @PrimaryKey val id: Int = 0,
    val value: Int
) 