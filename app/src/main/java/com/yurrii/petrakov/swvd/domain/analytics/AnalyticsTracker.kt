package com.yurrii.petrakov.swvd.domain.analytics

import com.yurrii.petrakov.swvd.domain.model.AnalyticsEvent
import kotlinx.coroutines.flow.Flow

interface AnalyticsTracker {

    val events: Flow<List<AnalyticsEvent>>
    fun trackEvent(
        name: String,
        message: String = ""
    )
}