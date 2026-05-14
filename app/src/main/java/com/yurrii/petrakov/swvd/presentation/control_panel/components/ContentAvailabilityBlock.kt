package com.yurrii.petrakov.swvd.presentation.control_panel.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yurrii.petrakov.swvd.presentation.components.TitleText
import com.yurrii.petrakov.swvd.presentation.control_panel.ControlPanelViewModel

@Composable
fun ContentAvailabilityBlock(
    controlPanelViewModel: ControlPanelViewModel
) {
    TitleText("Secure WebView Demo")

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Content Available")
        Spacer(Modifier.weight(1f))

        Switch(
            checked = controlPanelViewModel.contentAvailableState
                .collectAsStateWithLifecycle().value,
            onCheckedChange = controlPanelViewModel::onContentAvailableChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = Color(0xFF00A40B)
            )
        )
    }
}