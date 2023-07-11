package com.chrrissoft.workmanager.info.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo.State
import androidx.work.WorkQuery
import com.chrrissoft.workmanager.filterOut
import com.chrrissoft.workmanager.info.InfoState
import com.chrrissoft.workmanager.info.WorkQueryOwn
import com.chrrissoft.workmanager.info.components.InfoItem
import com.chrrissoft.workmanager.ui.components.MyInputChip
import java.util.*

@Composable
fun InfosByQueryPage(
    ids: Set<UUID>,
    tags: Set<String>,
    names: Set<String>,
    query: WorkQueryOwn,
    onChangeQuery: (WorkQueryOwn) -> Unit,
    onDelete: (WorkQuery) -> Unit,
    infos: InfoState.InfoByQuery,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        WorkQueryBuilder(
            id = ids,
            tags = tags,
            names = names,
            query = query,
            onChangeQuery = onChangeQuery
        )

        LazyColumn {
            infos.infos.forEach { (query, infos) ->
                item {
                    Column {
                        infos.forEach {
                            Spacer(modifier = Modifier.height(5.dp))
                            InfoItem(info = it) { onDelete(query) }
                        }
                        if (infos.isNotEmpty()) {
                            Divider(
                                color = colorScheme.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkQueryBuilder(
    id: Set<UUID>,
    tags: Set<String>,
    names: Set<String>,
    query: WorkQueryOwn,
    onChangeQuery: (WorkQueryOwn) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(shapes.medium)
            .background(colorScheme.primaryContainer)
            .padding(8.dp)
    ) {
        Text(
            text = "Work query builder",
            style = typography.titleLarge.copy(
                color = colorScheme.primary,
                fontWeight = Medium,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Divider(
            color = colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            Text(text = "ids:", color = colorScheme.primary)
            id.forEach { id ->
                MyInputChip(
                    label = { Text(id.hashCode().toString()) },
                    selected = query.ids.contains(id),
                    onClick = { selected ->
                        val idsToObserve = if (selected) query.ids.plus(id)
                        else query.ids.filterOut { it==id }
                        onChangeQuery(query.copy(ids = idsToObserve))
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            Text(text = "tags:", color = colorScheme.primary)
            tags.forEach { tag ->
                MyInputChip(
                    label = { Text(tag) },
                    selected = query.tags.contains(tag),
                    onClick = { selected ->
                        val tagsToObserve = if (selected) query.tags.plus(tag)
                        else query.tags.filterOut { it==tag }
                        onChangeQuery(query.copy(tags = tagsToObserve))
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            Text(text = "unique names:", color = colorScheme.primary)
            names.forEach { name ->
                MyInputChip(
                    label = { Text(name) },
                    selected = query.names.contains(name),
                    onClick = { selected ->
                        val namesToObserve = if (selected) query.names.plus(name)
                        else query.names.filterOut { it==name }
                        onChangeQuery(query.copy(names = namesToObserve))
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState()),
        ) {
            Text(text = "states:", color = colorScheme.primary)
            buildList {
                add(Pair(State.CANCELLED, Icons.Rounded.Info))
                add(Pair(State.FAILED, Icons.Rounded.Info))
                add(Pair(State.ENQUEUED, Icons.Rounded.Info))
                add(Pair(State.BLOCKED, Icons.Rounded.Info))
                add(Pair(State.RUNNING, Icons.Rounded.Info))
                add(Pair(State.SUCCEEDED, Icons.Rounded.Info))
            }.forEach { pair ->
                MyInputChip(
                    trailingIcon = { Icon(pair.second, (null)) },
                    label = { Text(pair.first.name.lowercase()) },
                    selected = query.states.contains(pair.first),
                    onClick = { selected ->
                        val states = if (selected) query.states.plus(pair.first)
                        else query.states.filterOut { it==pair.first }
                        onChangeQuery(query.copy(states = states))
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}
