package com.yurrii.petrakov.swvd.presentation.control_panel

import androidx.lifecycle.ViewModel
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.domain.repository.ContentGateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.io.encoding.Base64


class ControlPanelViewModel(
    val analyticsTracker: AnalyticsTracker,
    val contentGateRepository: ContentGateRepository
) : ViewModel() {
    private val _textLinkState = MutableStateFlow("")
    val textState: StateFlow<String> = _textLinkState.asStateFlow()

    private val _contentAvailableState = MutableStateFlow(contentGateRepository.isContentAvailable())
    val contentAvailableState: StateFlow<Boolean> = _contentAvailableState.asStateFlow()


    val defaultUrl = "aHR0cHM6Ly9leGFtcGxlLmNvbS8="

    fun onLinkTextChanged(newText: String) {
        _textLinkState.value = newText
    }

    fun onContentAvailableChange(newValue: Boolean) {
        _contentAvailableState.value = newValue
        contentGateRepository.setContentAvailable(newValue)
    }

    fun sendAnalyticsEvent() {
        analyticsTracker.trackEvent("custom_event", "test_send")
    }

    fun getEncodedUrl(): String {
        return if (_textLinkState.value.isEmpty()) {
            defaultUrl
        } else {
            Base64.encode(
                _textLinkState.value.toByteArray()
            )
        }
    }
}