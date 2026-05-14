package com.yurrii.petrakov.swvd.data.repository

import com.yurrii.petrakov.swvd.data.local.prefs.ContentGatePreferences
import com.yurrii.petrakov.swvd.domain.repository.ContentGateRepository

class ContentGateRepositoryImpl(
    private val preferences: ContentGatePreferences
) : ContentGateRepository {

    override fun isContentAvailable(): Boolean {
        return preferences.isContentAvailable()
    }

    override fun setContentAvailable(value: Boolean) {
        preferences.setContentAvailable(value)
    }
}