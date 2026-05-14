package com.yurrii.petrakov.swvd.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AnalyticsDao {

    @Insert
    suspend fun insert(event: AnalyticsEventEntity)

    @Query("SELECT * FROM analytics_events ORDER BY timestamp DESC")
    fun getAll(): Flow<List<AnalyticsEventEntity>>
}

