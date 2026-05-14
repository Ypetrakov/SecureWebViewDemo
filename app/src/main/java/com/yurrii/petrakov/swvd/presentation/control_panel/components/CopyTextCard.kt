package com.yurrii.petrakov.swvd.presentation.control_panel.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yurrii.petrakov.swvd.R


@Composable
fun CopyTextCard(
    text: String,
    onCopyClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF7F7F7),
                shape = RoundedCornerShape(16.dp)
            )
            .border(BorderStroke(1.dp, Color(0xFFEFEFEF)), RoundedCornerShape(25.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        IconButton(onClick = onCopyClick) {
            Icon(
                painter = painterResource(R.drawable.baseline_content_copy_24),
                contentDescription = "Copy",
                tint = Color.Black
            )
        }
    }
}