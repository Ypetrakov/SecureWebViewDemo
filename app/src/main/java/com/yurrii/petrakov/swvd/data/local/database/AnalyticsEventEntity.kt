package com.yurrii.petrakov.swvd.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yurrii.petrakov.swvd.domain.model.AnalyticsEvent

@Entity(tableName = "analytics_events")
data class AnalyticsEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val message: String,
    val timestamp: Long
)

fun AnalyticsEvent.toEntity(): AnalyticsEventEntity {
    return AnalyticsEventEntity(
        name = this.name,
        message = this.message,
        timestamp = timeStamp
    )
}

fun AnalyticsEventEntity.toDomain(): AnalyticsEvent {
    return AnalyticsEvent(
        name = this.name,
        message = this.message,
        timeStamp = this.timestamp
    )
}
