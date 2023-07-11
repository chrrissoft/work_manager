package com.chrrissoft.workmanager.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.work.WorkInfo
import androidx.work.WorkQuery
import com.chrrissoft.workmanager.filterOut
import com.chrrissoft.workmanager.info.InfoEvent.OnChangePage
import com.chrrissoft.workmanager.info.InfoEvent.OnInfoByQuery
import com.chrrissoft.workmanager.info.InfoState.InfoScreenPage.*
import com.chrrissoft.workmanager.info.pages.InfosByIdPage
import com.chrrissoft.workmanager.info.pages.InfosByQueryPage
import com.chrrissoft.workmanager.info.pages.InfosByTagPage
import com.chrrissoft.workmanager.info.pages.InfosByUniqueNamePage
import com.chrrissoft.workmanager.ui.components.*
import com.chrrissoft.workmanager.ui.theme.appBarColors
import com.chrrissoft.workmanager.ui.theme.navigationBarItemColors
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkInfoScreen(
    state: InfoState,
    onEvent: (InfoEvent) -> Unit,
    onOpenDrawer: () -> Unit
) {

    val (showIds, changeShowIds) = remember {
        mutableStateOf(false)
    }
    val (showTags, changeShowTags) = remember {
        mutableStateOf(false)
    }
    val (showNames, changeShowNames) = remember {
        mutableStateOf(false)
    }
    val (workQuery, changeQuery) = remember {
        mutableStateOf(WorkQueryOwn())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Work Info", fontWeight = FontWeight(600))
                },
                colors = appBarColors,
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colorScheme.primaryContainer,
            ) {
                listOf(ByID, ByTag, ByUniqueName, ByQuery).forEach {
                    NavigationBarItem(
                        colors = navigationBarItemColors,
                        selected = it==state.page,
                        onClick = {
                            onEvent(OnChangePage(it))
                        },
                        icon = {
                            Icon(it.icon, (null))
                        },
                        label = {
                            Text(it.text)
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (state.page) {
                        ByID -> changeShowIds(true)
                        ByTag -> changeShowTags(true)
                        ByUniqueName -> changeShowNames(true)
                        ByQuery -> {
                            if (workQuery.isEmpty) return@FloatingActionButton
                            onEvent(OnInfoByQuery(state.infosByQuery.infos.keys.plus(workQuery.toOriginal())))
                        }
                    }
                },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ) { Icon(Icons.Rounded.Add, (null)) }
        }
    ) { padding ->
        when (state.page) {
            ByID -> {
                InfosByIdPage(
                    allIds = state.observablesWorks.ids,
                    showIds = showIds,
                    onChangeShowIds = changeShowIds,
                    infos = state.infosById,
                    onRequestForInfo = { onEvent(it) },
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(colorScheme.onPrimary)
                )
            }
            ByTag -> {
                InfosByTagPage(
                    tags = state.observablesWorks.tags,
                    showTags = showTags,
                    onChangeShowTags = changeShowTags,
                    onRequest = {
                        onEvent(it)
                    },
                    infos = state.infosByTag,
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(colorScheme.onPrimary)
                )
            }
            ByUniqueName -> {
                InfosByUniqueNamePage(
                    names = state.observablesWorks.uniqueName,
                    showNames = showNames,
                    onChangeShowNames = changeShowNames,
                    onRequest = { onEvent(it) },
                    infos = state.infoByUniqueNames,
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(colorScheme.onPrimary)
                )
            }
            ByQuery -> {
                InfosByQueryPage(
                    query = workQuery,
                    onChangeQuery = changeQuery,
                    infos = state.infosByQuery,
                    ids = state.observablesWorks.ids,
                    tags = state.observablesWorks.tags,
                    names = state.observablesWorks.uniqueName,
                    onDelete = { query ->
                        onEvent(OnInfoByQuery(state.infosByQuery.infos.keys.filterOut { it==query }))
                    },
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(colorScheme.onPrimary)
                )
            }
        }
    }

}

data class WorkQueryOwn(
    val ids: Set<UUID> = emptySet(),
    val tags: Set<String> = emptySet(),
    val names: Set<String> = emptySet(),
    val states: Set<WorkInfo.State> = emptySet(),
) {

    val isEmpty = tags.isEmpty() && ids.isEmpty() && names.isEmpty() && states.isEmpty()

    fun toOriginal(): WorkQuery {
        return WorkQuery.Builder
            .fromIds(ids.toList())
            .addTags(tags.toList())
            .addStates(states.toList())
            .addUniqueWorkNames(names.toList())
            .build()
    }
}
