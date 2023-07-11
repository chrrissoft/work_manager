package com.chrrissoft.workmanager.request.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.request.RequestEvent
import com.chrrissoft.workmanager.request.RequestEvent.*
import com.chrrissoft.workmanager.request.state.RequestScreenPage.Builder
import com.chrrissoft.workmanager.request.state.RequestScreenPage.Requests
import com.chrrissoft.workmanager.request.state.RequestState
import com.chrrissoft.workmanager.request.state.ViewMode
import com.chrrissoft.workmanager.ui.theme.appBarColors
import com.chrrissoft.workmanager.ui.theme.navigationBarItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestBuilderScreen(
    state: RequestState,
    onEvent: (RequestEvent) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Work Requests", fontWeight = FontWeight(600))
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
                listOf(Builder, Requests).forEach {
                    NavigationBarItem(
                        colors = navigationBarItemColors,
                        selected = it==state.page,
                        onClick = {
                            onEvent(OnChangeRequestPage(it))
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
            val ctx = LocalContext.current
            FloatingActionButton(
                onClick = {
                    if (state.viewMode==ViewMode.View) {
                        onEvent(OnExitViewRequest)
                    } else {
                        onEvent(OnAddRequest(state.request))
                        Toast.makeText(ctx, "Request was added", Toast.LENGTH_SHORT).show()
                    }
                },
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
            ) {
                val icon = when (state.viewMode) {
                    ViewMode.Editing -> Icons.Rounded.Edit
                    ViewMode.Creating -> Icons.Rounded.Add
                    ViewMode.View -> Icons.Rounded.ArrowBack
                }
                Icon(icon, (null))
            }
        }
    ) { padding ->
        val modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(colorScheme.onPrimary)
        Column(
            modifier = if (state.page!=Builder) modifier
            else modifier.verticalScroll(rememberScrollState())
        ) {
            when (state.page) {
                Builder -> {
                    RequestBuilder(state = state) {
                        onEvent(it)
                    }
                    Spacer(Modifier.height(100.dp))
                }
                Requests -> {
                    RequestList(
                        requests = state.requests,
                        onEdit = {
                            onEvent(OnEditRequest(it))
                        },
                        onDelete = {
                            onEvent(OnDeleteRequest(it))
                        },
                        onView = {
                            onEvent(OnViewRequest(it))
                        },
                    )
                }
            }
        }
    }
}
