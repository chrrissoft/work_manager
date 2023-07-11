package com.chrrissoft.workmanager.request.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import com.chrrissoft.workmanager.ui.components.ItemTitle
import com.chrrissoft.workmanager.ui.components.ListItem
import com.chrrissoft.workmanager.ui.components.MyInputChip
import com.chrrissoft.workmanager.ui.components.RequestInfo

@Composable
fun RequestList(
    requests: List<WorkRequestOwn>,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
    onView: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .background(colorScheme.onPrimary)
    ) {
        items(requests) {
            Spacer(Modifier.height(8.dp))
            ListItem {
                ItemTitle(it.name)
                Spacer(Modifier.height(10.dp))
                RequestInfo(it.typeName, it.worker.text, it.expedited)
                Actions(
                    onEdit = { onEdit(it.id) },
                    onDelete = { onDelete(it.id) },
                    onView = { onView(it.id) }
                )
            }
        }
        item {
            Spacer(Modifier.height(8.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Actions(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onView: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val list = listOf(
            Triple("Edit", Icons.Rounded.Edit, onEdit),
            Triple("Delete", Icons.Rounded.Delete, onDelete),
            Triple("Full Info", Icons.Rounded.Info, onView),
        )
        list.forEachIndexed { index, triple ->
            MyInputChip(
                label = { Text(triple.first) },
                onClick = { triple.third() },
                modifier = Modifier
                    .weight(50f)
                    .padding(0.dp),
                leadingIcon = { Icon(triple.second, (null)) }
            )
            if (index!=list.lastIndex) {
                Box(Modifier.weight(3f))
            }
        }
    }
}
