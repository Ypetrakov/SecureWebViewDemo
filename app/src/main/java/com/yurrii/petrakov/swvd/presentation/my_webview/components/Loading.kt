package com.yurrii.petrakov.swvd.presentation.my_webview.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    isOpenedViaDeepLink: Boolean,
    url: String
) {

    var dots by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            dots = when (dots) {
                "" -> "."
                "." -> ".."
                ".." -> "..."
                else -> ""
            }

            delay(200)
        }
    }

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                Modifier.height(25.dp)
            ) {
                Text(
                    text = "Loading URL",
                    fontSize = 20.sp
                )
                Text(
                    text = dots,
                    fontSize = 24.sp,
                    modifier = Modifier.width(24.dp)
                )
            }



            if (isOpenedViaDeepLink) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Opened via deep link",
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF7F7F7),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(BorderStroke(1.dp, Color(0xFFEFEFEF)), RoundedCornerShape(25.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("URL (read only)",
                    color = Color(0xFF666666), textAlign = TextAlign.Center)

                Text(url, textAlign = TextAlign.Center)
            }
        }
        Spacer(Modifier.weight(1f))

    }
}