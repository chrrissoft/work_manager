package com.chrrissoft.workmanager.request.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.work.BackoffPolicy
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRow
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRowText
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog

@Composable
fun BackoffPolicy(
    policy: BackoffPolicy,
    policies: List<BackoffPolicy>,
    backoffMinutes: Float,
    onChangeBackoffCriteria: (BackoffPolicy, Float) -> Unit,
    modifier: Modifier = Modifier,
) {

    val (backoffDialog, onChangeBackoffDialog) = remember {
        mutableStateOf(false)
    }

    val (minutesValue, onChangeMinutesValue) = remember {
        mutableStateOf(backoffMinutes)
    }

    if (backoffDialog) {
        AlertDialog(
            onDismissRequest = {
                onChangeBackoffDialog(false)
                onChangeBackoffCriteria(policy, minutesValue)
            },
            onConfirm = {
                onChangeBackoffDialog(false)
                onChangeBackoffCriteria(policy, minutesValue)
            },
            content = {
                Text(
                    text = "backoff minutes: ${minutesValue.toInt()}",
                    color = colorScheme.primary,
                    fontWeight = FontWeight(600)
                )

                Slider(
                    value = minutesValue,
                    onValueChange = onChangeMinutesValue,
                    valueRange = 1f..60f
                )

                policies.forEach {
                    val selected = it == policy
                    DialogRadioChooserRow(
                        onClick = {
                            onChangeBackoffDialog(false)
                            onChangeBackoffCriteria(it, minutesValue)
                        },
                        content = {
                            RadioButton(selected, onClick = {
                                onChangeBackoffDialog(false)
                                onChangeBackoffCriteria(it, minutesValue)
                            })
                            DialogRadioChooserRowText(it.name.lowercase(), selected)
                        }
                    )
                }
            },
            title = {
                TitleAlertDialog(text = "Set Backoff Criteria")
            }
        )
    }

    Button(
        onClick = {
            onChangeBackoffDialog(true)
        },
        content = {
            Text(text = "Backoff policy: ${policy.name.lowercase()}   time: ${backoffMinutes.toInt()}m")
            Spacer(modifier = Modifier.width(10.dp))
            Icon(imageVector = Icons.Rounded.SettingsBackupRestore, contentDescription = null)
        },
        modifier = modifier
    )
}
