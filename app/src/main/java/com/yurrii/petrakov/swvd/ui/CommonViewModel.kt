package com.yurrii.petrakov.swvd.ui

import androidx.lifecycle.ViewModel
import com.yurrii.petrakov.swvd.ui.navigation.TopBarAction
import com.yurrii.petrakov.swvd.ui.navigation.TopBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommonViewModel: ViewModel() {
    private val topBarState_ = MutableStateFlow<TopBarState>(
        TopBarState(
            showBack = false,
            title = "Secure WebView Demo",
            showOptions = true,
            showTopBar = true
        )
    )
    val topBarState = topBarState_.asStateFlow()

    fun updateTopBarState(state: TopBarState) {
        topBarState_.value = state
    }

    fun updateBackVisibility(show: Boolean) {
        topBarState_.value = topBarState_.value.copy(
            showBack = show
        )
    }

    fun updateOptionsVisibility(show: Boolean) {
        topBarState_.value = topBarState_.value.copy(
            showOptions = show
        )
    }

    fun updateTitle(title: String) {
        topBarState_.value = topBarState_.value.copy(
            title = title
        )
    }

    fun setBackAction(action: () -> Unit) {
        topBarState_.value = topBarState_.value.copy(
            onBack = action
        )
    }

    fun setOptionsAction(action: () -> Unit) {
        topBarState_.value = topBarState_.value.copy(
            onOptions = action
        )
    }

    fun setVisibility(show: Boolean) {
        topBarState_.value = topBarState_.value.copy(
            showTopBar = show
        )
    }

    fun setOptionActions(actions: List<TopBarAction>) {
        topBarState_.value = topBarState_.value.copy(
            actions = actions
        )
    }
}