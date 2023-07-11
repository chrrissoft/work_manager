package com.chrrissoft.workmanager.request.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.OutOfQuotaPolicy
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRow
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRowText
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog

@Composable
fun OutOfQuotaPolicy(
    enabled: Boolean,
    policy: OutOfQuotaPolicy,
    policies: List<OutOfQuotaPolicy>,
    onChangePolicy: (OutOfQuotaPolicy) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (showDialog, onChangeShowDialog) = remember {
        mutableStateOf(false)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onChangeShowDialog(false)
            },
            onConfirm = {
                onChangeShowDialog(false)
            },
            title = {
                TitleAlertDialog(text = "Choose Policy")
            },
            content = {
                policies.forEach {
                    DialogRadioChooserRow(
                        onClick = {
                            onChangePolicy(it)
                            onChangeShowDialog(false)
                        },
                        content = {
                            val selected = it == policy
                            RadioButton(selected, onClick = {
                                onChangePolicy(it)
                                onChangeShowDialog(false)
                            })
                            DialogRadioChooserRowText(
                                text = it.name.replace("_", " ").lowercase(),
                                selected = selected
                            )
                        }
                    )
                }
            }
        )
    }

    Button(
        onClick = {
            onChangeShowDialog(true)
        },
        enabled = enabled,
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        Text(text = policy.name.replace("_", " ").lowercase())
    }
}