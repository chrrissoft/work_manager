package com.chrrissoft.workmanager.request.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SettingsInputComponent
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.request.state.InputMergerOwn
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRow
import com.chrrissoft.workmanager.ui.components.DialogRadioChooserRowText
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog


@Composable
fun InputMerger(
    enable: Boolean,
    merger: InputMergerOwn?,
    allowedMergers: List<InputMergerOwn>,
    onChangeMerger: (InputMergerOwn) -> Unit,
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
                TitleAlertDialog(text = "Choose Merger")
            },
            content = {
                allowedMergers.forEach {
                    DialogRadioChooserRow(
                        onClick = {
                            onChangeMerger(it)
                            onChangeShowDialog(false)
                        },
                        content = {
                            val selected = it == merger
                            RadioButton(selected, onClick = { onChangeMerger(it) })
                            DialogRadioChooserRowText(
                                text = it.mergerName, selected
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
        enabled = enable,
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        Text(text = merger?.mergerName ?: "Not allowed merger")
        Spacer(modifier = Modifier.width(10.dp))
        Icon(imageVector = Icons.Rounded.SettingsInputComponent, contentDescription = null)
    }
}
