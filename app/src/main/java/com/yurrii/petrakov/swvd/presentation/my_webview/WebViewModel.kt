package com.yurrii.petrakov.swvd.presentation.my_webview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebViewModel : ViewModel() {
    var progress by mutableFloatStateOf(0f)
    var title by mutableStateOf("")

    var url by mutableStateOf("")

    lateinit var onBackHandler: () -> Unit

    private val _webViewScreenState = MutableStateFlow<WebViewScreenState>(WebViewScreenState.Loading)
    val webViewScreenState = _webViewScreenState.asStateFlow()

    fun updateState(newState: WebViewScreenState) {
        _webViewScreenState.value = newState
    }
}


sealed interface WebViewScreenState{
    data object Loading: WebViewScreenState
    data object Success: WebViewScreenState
    data class ContentGate(val reason: String = "This content is not available in your region due to our policies."): WebViewScreenState
}