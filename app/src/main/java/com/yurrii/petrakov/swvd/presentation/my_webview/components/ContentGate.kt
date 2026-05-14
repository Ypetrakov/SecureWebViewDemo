package com.yurrii.petrakov.swvd.presentation.my_webview.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yurrii.petrakov.swvd.R
import com.yurrii.petrakov.swvd.presentation.components.BlueButton

@Composable
fun ContentGate(
    modifier: Modifier = Modifier,
    reason: String,
    backToSettings: () -> Unit
) {
    Box(
        modifier.fillMaxSize().padding(20.dp)
    ) {
        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.warning),
                contentDescription = "warning",
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.FillBounds
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Content unavailable",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = reason,
                fontSize = 18.sp,
                textAlign = TextAlign.Center

            )
            Spacer(Modifier.weight(1f))
        }
        BlueButton(
            text = "Back to settings",
            onClick = backToSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}