package com.yurrii.petrakov.swvd.presentation.control_panel.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yurrii.petrakov.swvd.presentation.components.BlueButton
import com.yurrii.petrakov.swvd.presentation.components.WhiteButton
import com.yurrii.petrakov.swvd.presentation.control_panel.ControlPanelViewModel

@Composable
fun WebViewBlock(
    controlPanelViewModel: ControlPanelViewModel,
    onLoad: () -> Unit,
    onClearCache: () -> Unit
) {
    OutlinedTextField(
        value = controlPanelViewModel.textState.collectAsStateWithLifecycle().value,
        onValueChange = controlPanelViewModel::onLinkTextChanged,
        label = { Text("Enter URL to load") },
        placeholder = { Text("https://example.com") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(40.dp),
        maxLines = 1
    )

    Spacer(Modifier.height(20.dp))

    BlueButton(
        text = "Open Secure WebView",
        onClick = onLoad
    )

    Spacer(Modifier.height(10.dp))

    WhiteButton(
        onClick = onClearCache,
        text = "Clear WebView Cache"
    )
}