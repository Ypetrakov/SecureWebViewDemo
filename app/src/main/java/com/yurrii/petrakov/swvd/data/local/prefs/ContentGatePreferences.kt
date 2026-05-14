package com.yurrii.petrakov.swvd.data.local.prefs

import android.content.Context

class ContentGatePreferences(context: Context) {

    private val prefs =
        context.getSharedPreferences(
            "content_gate_prefs",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val KEY_CONTENT_AVAILABLE =
            "key_content_available"
    }

    fun isContentAvailable(): Boolean {
        return prefs.getBoolean(
            KEY_CONTENT_AVAILABLE,
            true
        )
    }

    fun setContentAvailable(value: Boolean) {
        prefs.edit()
            .putBoolean(KEY_CONTENT_AVAILABLE, value)
            .apply()
    }
}