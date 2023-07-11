package com.chrrissoft.workmanager.request.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun IntervalsIU(
    flexMinutes: Float,
    flexEnabled: Boolean,
    intervalMinutes: Float,
    intervalEnabled: Boolean,
    onChangeFlexMinutes: (Float) -> Unit,
    onChangeIntervalMinutes: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {

    val (showSlider, onChangeShowSlider) = remember {
        mutableStateOf(false)
    }

    val (intervalSliderValue, onChangeIntervalSliderValue) = remember {
        mutableStateOf(15f)
    }
    val (flexSliderValue, onChangeFlexSliderValue) = remember {
        mutableStateOf(5f)
    }

    if (showSlider) {
        AlertDialog(
            onDismissRequest = {
                onChangeShowSlider(false)
            },
            confirmButton = {
                Button(onClick = {
                    onChangeIntervalMinutes(intervalSliderValue.roundToInt().toFloat())
                    onChangeFlexMinutes(flexSliderValue.roundToInt().toFloat())
                    onChangeShowSlider(false)

                }) {
                    Text(text = "Ok")
                }
            },
            title = {
                Text(text = "Choose Intervals")
            },
            titleContentColor = colorScheme.primary,
            text = {
                Column {
                    Divider(color = colorScheme.onPrimaryContainer)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Choose Interval: ${intervalSliderValue.toInt()}",
                        color = colorScheme.primary,
                        fontWeight = FontWeight(600)
                    )
                    Slider(
                        onValueChange = {
                            onChangeIntervalSliderValue(it)
                            if (flexSliderValue.plus(flexSliderValue.times(.2)) > intervalSliderValue)
                                onChangeFlexSliderValue(
                                    intervalSliderValue.minus(
                                        intervalSliderValue.times(.2f)
                                    )
                                )
                        },
                        value = intervalSliderValue,
                        valueRange = 15f..60f,
                        enabled = intervalEnabled,
                    )
                    Divider(Modifier.padding(top = 5.dp, bottom = 15.dp))
                    Text(
                        text = "Choose Flex Interval: ${flexSliderValue.toInt()}",
                        color = colorScheme.primary,
                        fontWeight = FontWeight(600)
                    )
                    Slider(
                        onValueChange = {
                            if (it.plus(it.times(.2)) < intervalSliderValue)
                                onChangeFlexSliderValue(it)
                        },
                        value = flexSliderValue,
                        valueRange = 5f..45f,
                        enabled = flexEnabled,
                    )

                }
            },
        )
    }

    PeriodicIntervalsButton(
        text = "Interval",
        onClick = { onChangeShowSlider(true) },
        minutes = intervalMinutes,
        enabled = intervalEnabled,
        modifier = modifier,
    )

    PeriodicIntervalsButton(
        text = "Flex",
        onClick = { onChangeShowSlider(true) },
        minutes = flexMinutes,
        enabled = flexEnabled,
        modifier = modifier,
    )
}

@Composable
private fun PeriodicIntervalsButton(
    text: String,
    onClick: () -> Unit,
    minutes: Float,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        Text(text = "$text $minutes m")
    }
}
