package com.chrrissoft.workmanager.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WorkHistory
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationDrawerItemDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chrrissoft.workmanager.info.InfoEvent
import com.chrrissoft.workmanager.info.InfoState
import com.chrrissoft.workmanager.info.WorkInfoScreen
import com.chrrissoft.workmanager.request.RequestEvent
import com.chrrissoft.workmanager.request.ui.RequestBuilderScreen
import com.chrrissoft.workmanager.ui.navigation.Screen.*
import com.chrrissoft.workmanager.works.events.AbstractWorksEvents
import com.chrrissoft.workmanager.works.events.WorksScreenStateEvent
import com.chrrissoft.workmanager.works.ui.EnqueueWorksScreen
import kotlinx.coroutines.launch
import com.chrrissoft.workmanager.request.state.RequestState as RequestBuilderScreenState
import com.chrrissoft.workmanager.works.state.ScreenState as EnqueuedWorksScreenState
import com.chrrissoft.workmanager.works.state.WorksState as EnqueuedWorksState


@Composable
fun AppNavigation(
    requestState: RequestBuilderScreenState,
    onRequestEvent: (RequestEvent) -> Unit,

    worksScreenState: EnqueuedWorksScreenState,
    onWorksScreenEvent: (WorksScreenStateEvent) -> Unit,

    enqueuedWorksState: EnqueuedWorksState,
    onWorksEvent: (AbstractWorksEvents) -> Unit,

    infoState: InfoState,
    onInfoEvent: (InfoEvent) -> Unit,
) {
    val controller = rememberNavController()
    val currentBackStack by controller.currentBackStackEntryAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorScheme.primaryContainer
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.WorkHistory,
                        contentDescription = (null),
                        tint = colorScheme.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text(
                    text = "Work Manager App",
                    fontWeight = Medium,
                    textAlign = TextAlign.Center,
                    style = typography.headlineLarge.copy(color = colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                listOf(RequestBuilderScreen, WorkManagerScreen, WorkInfoScreen).forEach {
                    val selected = it.route==currentBackStack?.destination?.route
                    NavigationDrawerItem(
                        selected = selected,
                        label = {
                            Text(it.name, fontWeight = if (selected) Bold else Normal)
                        },
                        icon = {
                            Icon(imageVector = it.icon, (null))
                        },
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                controller.navigate(it.route)
                            }
                        },
                        colors = run {
                            colors(
                                selectedContainerColor = colorScheme.primary,
                                unselectedContainerColor = colorScheme.onPrimary,
                                selectedIconColor = colorScheme.onPrimary,
                                unselectedIconColor = colorScheme.secondary.copy(.5f),
                                selectedTextColor = colorScheme.onPrimary,
                                unselectedTextColor = colorScheme.secondary.copy(.5f),
                            )
                        },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        },
        drawerState = drawerState,
    ) {
        NavHost(navController = controller, startDestination = RequestBuilderScreen.route) {
            composable(RequestBuilderScreen.route) {
                RequestBuilderScreen(
                    state = requestState,
                    onEvent = { onRequestEvent(it) },
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
            composable(WorkManagerScreen.route) {
                EnqueueWorksScreen(
                    state = worksScreenState,
                    worksState = enqueuedWorksState,
                    onWorksEvent = {
                        onWorksEvent(it)
                    },
                    requests = requestState.requests,
                    onStateEvent = {
                        onWorksScreenEvent(it)
                    },
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
            composable(WorkInfoScreen.route) {
                WorkInfoScreen(
                    state = infoState,
                    onEvent = { onInfoEvent(it) },
                    onOpenDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        }
    }
}
