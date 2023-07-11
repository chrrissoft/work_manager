package com.chrrissoft.workmanager.request.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.*
import androidx.compose.material3.IconButtonDefaults.filledIconToggleButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.chrrissoft.workmanager.request.state.RequestType
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRow
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRowText
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog
import com.chrrissoft.workmanager.workers.WorkerType
import com.chrrissoft.workmanager.workers.WorkerType.CoroutineWorker
import com.chrrissoft.workmanager.workers.WorkerType.Worker

@Composable
fun Changers(
    expedited: Boolean,
    expeditedEnabled: Boolean,
    flexInterval: Boolean,
    flexIntervalAllowed: Boolean,
    workerType: WorkerType,
    requestType: RequestType,
    onChangeWorkerType: (WorkerType) -> Unit,
    onChangeRequestType: (RequestType) -> Unit,
    onChangeFlexInterval: (Boolean) -> Unit,
    onChangeExpedited: (Boolean) -> Unit,
) {
    val (showWorkerType, onChangeShowWorkerType) = remember {
        mutableStateOf(false)
    }

    val (showRequestType, onChangeShowRequestType) = remember {
        mutableStateOf(false)
    }

    if (showRequestType) {
        AlertDialog(
            title = {
                Text(text = "Choose request type")
            },
            onDismissRequest = { onChangeShowRequestType(false) },
            content = {
                listOf(RequestType.OneTime, RequestType.Periodic).forEach {
                    DialogRadioChooserRow(
                        onClick = {
                            onChangeRequestType(it)
                            onChangeShowRequestType(false)
                        },
                        content = {
                            val selected = requestType==it
                            RadioButton(selected, onClick = {
                                    onChangeRequestType(it)
                                    onChangeShowRequestType(false)
                                })
                            DialogRadioChooserRowText(it.text, selected)
                        }
                    )
                }
            },
            onConfirm = {
                onChangeShowRequestType(false)
            }
        )
    }

    if (showWorkerType) {
        AlertDialog(
            title = {
                TitleAlertDialog(text = "Choose worker type")
            },
            onDismissRequest = { onChangeShowWorkerType(false) },
            content = {
                listOf(Worker, CoroutineWorker).forEach {
                    DialogRadioChooserRow(
                        content = {
                            val selected = workerType==it
                            RadioButton(selected = selected, onClick = {
                                onChangeWorkerType(it)
                                onChangeShowWorkerType(false)
                            })
                            DialogRadioChooserRowText(it.text, selected)
                        },
                        onClick = {
                            onChangeWorkerType(it)
                            onChangeShowWorkerType(false)
                        }
                    )
                }
            },
            onConfirm = {
                onChangeShowWorkerType(false)
            }
        )
    }

    val colors = filledIconToggleButtonColors(
        contentColor = colorScheme.onPrimaryContainer,
        containerColor = colorScheme.primaryContainer,
        checkedContentColor = colorScheme.primaryContainer,
        checkedContainerColor = colorScheme.onPrimaryContainer,
    )


    FilledIconToggleButton(
        checked = if (expeditedEnabled) expedited else false,
        colors = colors,
        enabled = expeditedEnabled,
        onCheckedChange = {
            onChangeExpedited(it)
        }
    ) { Icon(imageVector = Icons.Rounded.Speed, (null)) }

    FilledIconToggleButton(
        checked = flexInterval,
        colors = colors,
        enabled = flexIntervalAllowed,
        onCheckedChange = {
            onChangeFlexInterval(it)
        }
    ) { Icon(imageVector = Icons.Rounded.HourglassTop, (null)) }

    Button(onClick = {
        onChangeShowRequestType(true)
    }) {
        val text = when (requestType) {
            RequestType.OneTime -> "One time"
            RequestType.Periodic -> "Periodic"
        }
        Text(text)
    }
    Button(onClick = {
        onChangeShowWorkerType(true)
    }) {
        Text(workerType.text)
    }
}
