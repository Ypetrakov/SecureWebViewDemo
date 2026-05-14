package com.yurrii.petrakov.swvd.presentation.control_panel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.presentation.control_panel.components.AnalyticsBlock
import com.yurrii.petrakov.swvd.presentation.control_panel.components.ContentAvailabilityBlock
import com.yurrii.petrakov.swvd.presentation.control_panel.components.DeepLinkBlock
import com.yurrii.petrakov.swvd.presentation.control_panel.components.WebViewBlock
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    controlPanelViewModel: ControlPanelViewModel = koinViewModel(),
    loadWebview: (String) -> Unit,
    clearCache: () -> Unit
) {
    val analyticsTracker: AnalyticsTracker = koinInject()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        WebViewBlock(
            controlPanelViewModel = controlPanelViewModel,
            onLoad = { loadWebview(controlPanelViewModel.getEncodedUrl()) },
            onClearCache = clearCache
        )

        Spacer(Modifier.height(24.dp))

        ContentAvailabilityBlock(controlPanelViewModel)

        Spacer(Modifier.height(24.dp))

        AnalyticsBlock(controlPanelViewModel)

        Spacer(Modifier.height(24.dp))

        DeepLinkBlock(analyticsTracker)
    }
}

