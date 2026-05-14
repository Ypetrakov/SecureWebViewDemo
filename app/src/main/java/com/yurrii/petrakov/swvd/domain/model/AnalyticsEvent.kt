package com.yurrii.petrakov.swvd.domain.model


data class AnalyticsEvent(
    val name: String,
    val message: String,
    val timeStamp: Long
)