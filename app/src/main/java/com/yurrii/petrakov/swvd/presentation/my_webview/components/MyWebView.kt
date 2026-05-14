package com.yurrii.petrakov.swvd.presentation.my_webview.components

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.yurrii.petrakov.swvd.presentation.my_webview.WebViewModel

@Composable
fun MyWebView(
    viewModel: WebViewModel,
    webView: WebView,
    openedWithDeepLink: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        if (openedWithDeepLink) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color(0xFF2100B6), RoundedCornerShape(25.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text("Opened via deep link", color = Color.White)
            }
        }
        Box(
            Modifier
                .height(65.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            if (viewModel.progress == 1f) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF7F7F7))
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            drawLine(
                                color = Color.LightGray,
                                start = Offset(0f, size.height - strokeWidth / 2),
                                end = Offset(size.width, size.height - strokeWidth / 2),
                                strokeWidth = strokeWidth
                            )
                        }
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        viewModel.title,
                        color = Color(0xFF1A73E8), fontSize = 18.sp
                    )
                    Spacer(Modifier.width(5.dp))
                    Text(viewModel.url)
                }
            } else {
                LinearProgressIndicator(
                    progress = { viewModel.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    color = Color(0xFF2100B6),
                    trackColor = Color(0xFFE8DEF8),
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
            }

        }
        AndroidView(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            factory = {
                webView
            }
        )
    }

}