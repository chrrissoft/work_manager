package com.chrrissoft.workmanager.works.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.Operation
import com.chrrissoft.workmanager.ui.components.ItemTitle
import com.chrrissoft.workmanager.ui.components.ListItem
import com.chrrissoft.workmanager.ui.components.MyInputChip
import com.chrrissoft.workmanager.works.state.OperationOwn

@Composable
fun OperationsPage(
    operationOwn: List<OperationOwn>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .background(colorScheme.onPrimary)
    ) {
        items(operationOwn) {
            Spacer(Modifier.height(8.dp))
            ListItem {
                ItemTitle(text = it.name)
                OperationInfo(it.operation.state.observeAsState().value, it.type)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OperationInfo(
    state: Operation.State?,
    type: OperationOwn.Type,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        MyInputChip(
            label = {
                Text(text = "Type: $type")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Rounded.Info, (null))
            },
            modifier = Modifier
                .weight(10f)
                .padding(end = 3.dp)
        )
        MyInputChip(
            label = {
                Text(text = "State: $state")
            },
            trailingIcon = {
                Icon(imageVector = Icons.Rounded.Info, (null))
            },
            modifier = Modifier
                .weight(10f)
                .padding(start = 3.dp)
        )
    }
}
