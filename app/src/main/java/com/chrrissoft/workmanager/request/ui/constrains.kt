package com.chrrissoft.workmanager.request.ui

import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.IconButtonDefaults.filledIconToggleButtonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.work.NetworkType
import androidx.work.NetworkType.*
import com.chrrissoft.workmanager.request.state.ConstraintsOwn
import com.chrrissoft.workmanager.ui.components.*

private val NetworkType.netName
    get() = run {
        this.name.replace("_", "").lowercase()
    }

private val types = listOf(
    NOT_REQUIRED,
    CONNECTED, UNMETERED, NOT_ROAMING,
    METERED,
).addIf(TEMPORARILY_UNMETERED) {
    Build.VERSION.SDK_INT >= VERSION_CODES.R
}

private fun <E> List<E>.addIf(element: E, predicate: () -> Boolean): List<E> {
    return if (predicate()) this.plus(element) else this
}

@Composable
fun ConstraintsUi(
    constraints: ConstraintsOwn,
    onChangeConstraints: (ConstraintsOwn) -> Unit,
) {

    val (netTypeDialog, onChangeNetTypeDialog) = remember {
        mutableStateOf(false)
    }

    if (netTypeDialog) {
        AlertDialog(
            content = {
                types.forEach {
                    DialogRadioChooserRow(
                        onClick = {
                            onChangeConstraints(constraints.copy(requiredNetworkType = it))
                            onChangeNetTypeDialog(false)
                        },
                        content = {
                            val selected = it==constraints.requiredNetworkType
                            RadioButton(
                                selected, onClick = {
                                    onChangeConstraints(constraints.copy(requiredNetworkType = it))
                                    onChangeNetTypeDialog(false)
                                }
                            )
                            DialogRadioChooserRowText(it.netName, selected)
                        }
                    )
                }
            },
            title = {
                TitleAlertDialog(text = "Set Required Network Type")
            },
            onDismissRequest = {
                onChangeNetTypeDialog(false)
            },
            onConfirm = {
                onChangeNetTypeDialog(false)
            }
        )
    }

    Button(onClick = { onChangeNetTypeDialog(true) }) {
        Text(text = constraints.requiredNetworkType.netName, style = typography.labelMedium)
        Icon(imageVector = Rounded.NetworkCell, contentDescription = null)
    }

    ChecksButtonsConstraints(constraints) {
        onChangeConstraints(it)
    }
}

@Composable
private fun ChecksButtonsConstraints(
    constraints: ConstraintsOwn,
    onChangeConstraints: (ConstraintsOwn) -> Unit,
) {
    val colors = filledIconToggleButtonColors(
        contentColor = colorScheme.onPrimaryContainer,
        containerColor = colorScheme.primaryContainer,
        checkedContentColor = colorScheme.primaryContainer,
        checkedContainerColor = colorScheme.onPrimaryContainer,
    )

    CenterRow {
        FilledIconToggleButton(
            checked = constraints.requiresCharging,
            onCheckedChange = {
                onChangeConstraints(constraints.copy(requiresCharging = it))
            },
            content = { Icon(Rounded.BatteryChargingFull, (null)) },
            colors = colors
        )
        FilledIconToggleButton(
            checked = constraints.requiresDeviceIdle,
            onCheckedChange = {
                onChangeConstraints(constraints.copy(requiresDeviceIdle = it))
            },
            content = { Icon(Rounded.ModeStandby, (null)) },
            colors = colors
        )
        FilledIconToggleButton(
            checked = constraints.requiresBatteryNotLow,
            onCheckedChange = {
                onChangeConstraints(constraints.copy(requiresBatteryNotLow = it))
            },
            content = { Icon(Rounded.BatteryFull, (null)) },
            colors = colors
        )
        FilledIconToggleButton(
            checked = constraints.requiresStorageNotLow,
            onCheckedChange = {
                onChangeConstraints(constraints.copy(requiresStorageNotLow = it))
            },
            content = { Icon(Rounded.Save, (null)) },
            colors = colors
        )
    }
}
