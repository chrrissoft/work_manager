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
fun TagsUi(
    tag: Set<String>,
    onAddTag: (String) -> Unit,
    onDeleteTag: (String) -> Unit,
) {

    val (showInput, onChangeShowInput) = remember {
        mutableStateOf(false)
    }

    val (input, onChangeInput) = remember {
        mutableStateOf("")
    }

    if (showInput) {
        AlertDialog(
            onDismissRequest = { onChangeShowInput(false) },
            content = {
                MyTextField(
                    value = input,
                    onValueChange = onChangeInput,
                    placeholder = {
                        Text(text = "Tag")
                    }
                )
            },
            title = {
                TitleAlertDialog(text = "Write a tag")
            },
            onConfirm = {
                if (input.isNotEmpty()) onAddTag(input)
                onChangeShowInput(false)
            }
        )
    }

    Button(
        onClick = {
            onChangeShowInput(true)
            onChangeInput("")
        }
    ) {
        Text(text = "Add Tag", style = MaterialTheme.typography.labelMedium)
    }
    Spacer(Modifier.width(10.dp))
    TagsList(
        tag = tag,
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
fun TagsList(
    tag: Set<String>,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier.clip(shapes.extraLarge)) {
        items(tag.toList()) {
            MyInputChip(
                onClick = { _ -> onEdit(it) },
                label = {
                    Text(text = it)
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable { onDelete(it) }
                    )
                },
            )
            Spacer(modifier = Modifier.width(7.dp))
        }
    }
}
