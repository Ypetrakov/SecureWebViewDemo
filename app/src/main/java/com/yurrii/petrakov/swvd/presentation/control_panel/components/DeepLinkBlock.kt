package com.yurrii.petrakov.swvd.presentation.control_panel.components

import android.content.ClipData
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.yurrii.petrakov.swvd.domain.analytics.AnalyticsTracker
import com.yurrii.petrakov.swvd.presentation.components.TitleText
import com.yurrii.petrakov.swvd.presentation.components.WhiteButton


@Composable
fun DeepLinkBlock(
    analyticsTracker: AnalyticsTracker
) {
    TitleText("Secure DeepLink")

    Spacer(Modifier.height(10.dp))

    val clipboardManager = LocalClipboard.current
    val context = LocalContext.current

    val deepLinkUrl = remember {
        "myapp://game?url=aHR0cHM6Ly9uZXdzLnljb21iaW5hdG9yLmNvbS8=&title=Test"
    }

    CopyTextCard(deepLinkUrl) {
        val clip = ClipData.newPlainText(
            "Game Link",
            "myapp://game?url=aHR0cHM6Ly9uZXdzLnljb21iaW5hdG9yLmNvbS8=&title=Test"
        )
        clipboardManager.nativeClipboard.setPrimaryClip(clip)
        analyticsTracker.trackEvent("deep_link_copied")
    }

    WhiteButton(
        onClick = {
            try {
                val intent = Intent(Intent.ACTION_VIEW, deepLinkUrl.toUri()).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "No app found to handle this link",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        text = "Simulate Deep Link"
    )
}