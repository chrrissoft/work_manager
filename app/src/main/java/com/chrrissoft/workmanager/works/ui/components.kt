package com.chrrissoft.workmanager.works.ui

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Minimize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import com.chrrissoft.workmanager.ui.components.ItemTitle
import com.chrrissoft.workmanager.ui.components.MyInputChip
import com.chrrissoft.workmanager.ui.theme.textFieldColors
import java.util.*

@Composable
fun WorkRequests(
    requests: Map<UUID, List<WorkRequestOwn>>,
    onPop: (UUID) -> Unit,
    onDelete: (UUID) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        requests.forEach {
            Spacer(Modifier.height(8.dp))
            ItemTitle(
                text = "${it.value.first().name}  -  ${it.value.size}",
                actions = {
                    Icon(
                        imageVector = Icons.Rounded.Minimize,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { onPop(it.key) }
                    )
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { onDelete(it.key) }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorksActions(
    onRun: () -> Unit,
    onDelete: () -> Unit,
    onShowRequests: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val list = listOf(
            Triple("Run", Icons.Rounded.Add, onRun),
            Triple("Delete", Icons.Rounded.Info, onDelete),
            Triple("Requests", Icons.Rounded.Info, onShowRequests),
        )
        list.forEachIndexed { index, item ->
            MyInputChip(
                label = { Text(item.first) },
                leadingIcon = { Icon(item.second, (null)) },
                onClick = { item.third() },
                modifier = Modifier.weight(50f),
            )
            if (index!=list.lastIndex) {
                Box(Modifier.weight(3f))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestActions(
    onAdd: () -> Unit,
    onInfo: () -> Unit,
    modifier: Modifier = Modifier,
    onFullInfo: (() -> Unit)? = null,
) {
    val ctx = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val list = listOf(
            Triple("Add", Icons.Rounded.Add, onAdd),
            Triple("Info", Icons.Rounded.Info, onInfo),
            Triple("Full info", Icons.Rounded.Info, onFullInfo),
        )
        list.forEachIndexed { index, item ->
            MyInputChip(
                label = { Text(item.first) },
                leadingIcon = { Icon(item.second, (null)) },
                onClick = {
                    item.third.let {
                        if (it==null) Toast.makeText(ctx, "TODO 😉", LENGTH_SHORT).show()
                        else it()
                    }
                },
                modifier = Modifier.weight(50f),
            )
            if (index!=list.lastIndex) {
                Box(Modifier.weight(3f))
            }
        }
    }
}

@Composable
fun WorksTitleInput(
    name: String,
    onChangeName: (String) -> Unit,
) {
    TextField(
        value = name,
        onValueChange = {
            onChangeName(it)
        },
        shape = MaterialTheme.shapes.medium,
        maxLines = 1,
        colors = textFieldColors,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = Done)
    )
}
