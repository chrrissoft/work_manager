package com.chrrissoft.workmanager.request.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.MyInputChip
import com.chrrissoft.workmanager.ui.components.MyTextField
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog

@Composable
fun DataUi(
    data: Map<String, String>,
    onAddData: (Pair<String, String>) -> Unit,
    onDeleteTag: (String) -> Unit,
) {

    val (showInput, onChangeShowInput) = remember {
        mutableStateOf(false)
    }

    val (input, onChangeInput) = remember {
        mutableStateOf(Pair("", ""))
    }

    if (showInput) {
        AlertDialog(
            onDismissRequest = { onChangeShowInput(false) },
            content = {
                MyTextField(
                    value = input.first,
                    onValueChange = {
                        onChangeInput(input.copy(first = it))
                    },
                    placeholder = {
                        Text(text = "Key")
                    }
                )
                MyTextField(
                    value = input.second,
                    onValueChange = {
                        onChangeInput(input.copy(second = it))
                    },
                    placeholder = {
                        Text(text = "Value")
                    }
                )
            },
            title = {
                TitleAlertDialog(text = "Create a key value pair")
            },
            onConfirm = {
                if (input.first.isNotEmpty() && input.second.isNotEmpty()) onAddData(input)
                onChangeShowInput(false)
            }
        )
    }

    Button(
        onClick = {
            onChangeShowInput(true)
            onChangeInput(Pair("", ""))
        }
    ) {
        Text(text = "Add Data", style = MaterialTheme.typography.labelMedium)
    }
    Spacer(Modifier.width(10.dp))
    DataList(
        tag = data,
        onEdit = {
            onChangeInput(it)
            onChangeShowInput(true)
        },
        onDelete = {
            onDeleteTag(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataList(
    tag: Map<String, String>,
    onEdit: (Pair<String, String>) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier.clip(shapes.extraLarge)) {
        items(tag.toList()) {
            MyInputChip(
                label = {
                    Text(text = "${it.first} to ${it.second}")
                },
                onClick = { _ ->
                    onEdit(it)
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable { onDelete(it.first) }
                    )
                },
            )
        }
    }
}
