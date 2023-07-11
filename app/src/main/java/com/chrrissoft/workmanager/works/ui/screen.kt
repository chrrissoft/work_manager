package com.chrrissoft.workmanager.works.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.chrrissoft.workmanager.request.state.OneTimeWorkRequestOwn
import com.chrrissoft.workmanager.request.state.PeriodicWorkRequestOwn
import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import com.chrrissoft.workmanager.ui.theme.appBarColors
import com.chrrissoft.workmanager.ui.theme.navigationBarItemColors
import com.chrrissoft.workmanager.works.events.AbstractWorksEvents
import com.chrrissoft.workmanager.works.events.UniqueOneTimeWorksEvents.OnEnqueueUniqueOneTimeWorks
import com.chrrissoft.workmanager.works.events.UniquePeriodicWorksEvent.OnEnqueueUniquePeriodicWork
import com.chrrissoft.workmanager.works.events.WorksEvent.OnEnqueueWorks
import com.chrrissoft.workmanager.works.events.WorksScreenStateEvent
import com.chrrissoft.workmanager.works.state.EnqueueWorksScreenPages.*
import com.chrrissoft.workmanager.works.state.ScreenState
import com.chrrissoft.workmanager.works.state.WorksState
import com.chrrissoft.workmanager.works.ui.pages.OperationsPage
import com.chrrissoft.workmanager.works.ui.pages.UniqueOneTimeWorksPage
import com.chrrissoft.workmanager.works.ui.pages.UniquePeriodicWorksPage
import com.chrrissoft.workmanager.works.ui.pages.WorksPage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnqueueWorksScreen(
    requests: List<WorkRequestOwn>,
    worksState: WorksState,
    onWorksEvent: (AbstractWorksEvents) -> Unit,
    state: ScreenState,
    onStateEvent: (WorksScreenStateEvent) -> Unit,
    onOpenDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Enqueue Works", fontWeight = FontWeight(600))
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
                listOf(Works, UniqueOneTimeWorks, UniquePeriodicWorks, Operations).forEach {
                    NavigationBarItem(
                        colors = navigationBarItemColors,
                        selected = it==state.page,
                        onClick = {
                            onStateEvent(WorksScreenStateEvent.OnChangePage(it))
                        },
                        icon = {
                            Icon(it.icon, (null))
                        },
                        label = {
                            Text(text = it.text)
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (state.page) {
                        Works -> {
                            onWorksEvent(OnEnqueueWorks(worksState.works.size.plus(1)))
                        }
                        UniqueOneTimeWorks -> {
                            onWorksEvent(OnEnqueueUniqueOneTimeWorks(worksState.uniquesOneTimeWorks.size.plus(
                                1)))
                        }
                        UniquePeriodicWorks -> {
                            onWorksEvent(OnEnqueueUniquePeriodicWork(worksState.uniquesPeriodicWorks.size.plus(
                                1)))
                        }
                        Operations -> {}
                    }
                },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
            ) {
                Icon(imageVector = Icons.Rounded.Add, (null))
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.onPrimary)
        ) {
            when (state.page) {
                Works -> {
                    WorksPage(
                        requests = requests,
                        screenState = state,
                        onStateEvent = { onStateEvent(it) },
                        enqueuedWorks = worksState.works,
                        onWorksEvent = { onWorksEvent(it) },
                        modifier = Modifier.padding(padding)
                    )
                }
                UniqueOneTimeWorks -> {
                    UniqueOneTimeWorksPage(
                        requests = requests.filterIsInstance(OneTimeWorkRequestOwn::class.java),
                        screenState = state,
                        onStateEvent = { onStateEvent(it) },
                        enqueuedWorks = worksState.uniquesOneTimeWorks,
                        onWorksEvent = { onWorksEvent(it) },
                        modifier = Modifier.padding(padding)
                    )
                }
                UniquePeriodicWorks -> {
                    UniquePeriodicWorksPage(
                        requests = requests.filterIsInstance(PeriodicWorkRequestOwn::class.java),
                        screenState = state,
                        onStateEvent = { onStateEvent(it) },
                        enqueuedWorks = worksState.uniquesPeriodicWorks,
                        onWorksEvent = { onWorksEvent(it) },
                        modifier = Modifier.padding(padding)
                    )
                }
                Operations -> {
                    OperationsPage(
                        operationOwn = worksState.operations,
                        Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
