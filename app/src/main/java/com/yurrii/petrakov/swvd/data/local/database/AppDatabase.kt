package com.yurrii.petrakov.swvd.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [AnalyticsEventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun analyticsDao(): AnalyticsDao

}

fun createDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(context, AppDatabase::class.java, "app-database").build()

fun createAnalyticsDao(database: AppDatabase): AnalyticsDao = database.analyticsDao()
