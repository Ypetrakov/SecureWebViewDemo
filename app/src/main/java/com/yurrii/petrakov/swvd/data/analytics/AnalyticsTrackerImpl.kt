package com.yurrii.petrakov.swvd.data.analytics

import android.util.Log
import com.yurrii.petrakov.swvd.data.local.database.toDomain
import com.yurrii.petrakov.swvd.data.local.database.toEntity
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.domain.model.AnalyticsEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.time.Clock

class AnalyticsTrackerImpl(
    private val repository: AnalyticsRepository
) : AnalyticsTracker {

    companion object {
        private const val TAG = "[APP_ANALYTICS]"
    }

    override val events: Flow<List<AnalyticsEvent>> = repository.events().map { list ->
        list.map { it.toDomain() }
    }

    override fun trackEvent(
        name: String,
        message: String
    ) {

        val event = AnalyticsEvent(
            name = name,
            message = message,
            timeStamp = Clock.System.now().toEpochMilliseconds()
        )

        Log.d(TAG, buildLogMessage(event))
        CoroutineScope(Dispatchers.IO).launch {
            repository.save(event.toEntity())
        }    }

    private fun buildLogMessage(event: AnalyticsEvent): String {
        return if (event.message.isEmpty()) event.name
        else "${event.name}: ${event.message}"
    }

}

