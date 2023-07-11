package com.chrrissoft.workmanager.request.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreTime
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog

@Composable
fun InitialDelayUI(
    delay: Float,
    modifier: Modifier = Modifier,
    onChangeDelay: (Float) -> Unit,
) {
    val (delayDialog, onChangeDelayDialog) = remember {
        mutableStateOf(false)
    }

    val (delayValue, onChangeDelayValue) = remember {
        mutableStateOf(delay)
    }

    if (delayDialog) {
        AlertDialog(
            title = {
                TitleAlertDialog(text = "Set Initial Delay")
            },
            onDismissRequest = {
                onChangeDelayDialog(false)
                onChangeDelay(delayValue)
            },
            onConfirm = {
                onChangeDelayDialog(false)
                onChangeDelay(delayValue)
            },
            content = {
                Text(
                    text = "Initial Delay ${delayValue.toInt()}",
                    color = colorScheme.primary,
                    fontWeight = FontWeight(600)
                )
                Slider(
                    value = delayValue,
                    onValueChange = onChangeDelayValue,
                    valueRange = 0f..60f
                )
            }
        )
    }

    Button(
        onClick = {
            onChangeDelayDialog(true)
        },
        content = {
            Text(text = "Initial delay: ${delay.toInt()} s")
            Spacer(modifier = Modifier.width(10.dp))
            Icon(imageVector = Icons.Rounded.MoreTime, contentDescription = null)
        },
        modifier = modifier
    )
}
