package com.yurrii.petrakov.swvd.data.analytics

import com.yurrii.petrakov.swvd.data.local.database.AnalyticsDao
import com.yurrii.petrakov.swvd.data.local.database.AnalyticsEventEntity
import kotlinx.coroutines.flow.Flow

class AnalyticsRepository(
    private val dao: AnalyticsDao
) {

    fun events(): Flow<List<AnalyticsEventEntity>> = dao.getAll()

    suspend fun save(event: AnalyticsEventEntity) {
        dao.insert(event)
    }
}