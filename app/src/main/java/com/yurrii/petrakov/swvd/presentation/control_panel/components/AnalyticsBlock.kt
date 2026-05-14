package com.yurrii.petrakov.swvd.presentation.control_panel.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yurrii.petrakov.swvd.helper.formatTime
import com.yurrii.petrakov.swvd.presentation.components.TitleText
import com.yurrii.petrakov.swvd.presentation.components.WhiteButton
import com.yurrii.petrakov.swvd.presentation.control_panel.ControlPanelViewModel

@Composable
fun ColumnScope.AnalyticsBlock(
    controlPanelViewModel: ControlPanelViewModel
) {
    TitleText("Analytics")

    Spacer(Modifier.height(10.dp))

    WhiteButton(
        onClick = controlPanelViewModel::sendAnalyticsEvent,
        text = "Send Analytics Event"
    )

    Spacer(Modifier.height(10.dp))

    val events = controlPanelViewModel.analyticsTracker
        .events
        .collectAsStateWithLifecycle(initialValue = emptyList())
        .value

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(Color(0xFFF7F7F7), RoundedCornerShape(25.dp))
            .border(BorderStroke(1.dp, Color(0xFFEFEFEF)), RoundedCornerShape(25.dp))
            .padding(16.dp)
    ) {
        Text(
            text = events.joinToString("\n") { event ->
                "[${formatTime(event.timeStamp)}] ${event.name}: ${event.message}"
            },
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        )
    }
}