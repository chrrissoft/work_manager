package com.chrrissoft.workmanager.works.ui.pages

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import com.chrrissoft.workmanager.ui.components.ItemTitle
import com.chrrissoft.workmanager.ui.components.ListItem
import com.chrrissoft.workmanager.ui.components.RequestInfo
import com.chrrissoft.workmanager.works.addRequestToEnqueueToast
import com.chrrissoft.workmanager.works.events.WorksEvent
import com.chrrissoft.workmanager.works.events.WorksEvent.*
import com.chrrissoft.workmanager.works.events.WorksScreenStateEvent
import com.chrrissoft.workmanager.works.state.ScreenState
import com.chrrissoft.workmanager.works.state.Works
import com.chrrissoft.workmanager.works.ui.RequestActions
import com.chrrissoft.workmanager.works.ui.WorkRequests
import com.chrrissoft.workmanager.works.ui.WorksActions
import com.chrrissoft.workmanager.works.ui.WorksTitleInput

@Composable
fun WorksPage(
    requests: List<WorkRequestOwn>,
    screenState: ScreenState,
    onStateEvent: (WorksScreenStateEvent) -> Unit,
    enqueuedWorks: List<Works>,
    onWorksEvent: (WorksEvent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val ctx = LocalContext.current

    LazyColumn(modifier) {
        enqueuedWorks.forEach {
            item {
                val (showRequests, changeShowRequests) = rememberSaveable {
                    mutableStateOf(true)
                }
                Spacer(Modifier.height(8.dp))
                ListItem(
                    selected = it===screenState.selectedWorks,
                    onClick = {
                        onStateEvent(WorksScreenStateEvent.OnChangeSelectedWorks(it))
                    }
                ) {
                    WorksTitleInput(
                        name = it.name,
                        onChangeName = { name ->
                            onWorksEvent(OnChangeWorksName(it, name))
                        }
                    )
                    if (showRequests) {
                        Spacer(Modifier.height(10.dp))
                        WorkRequests(
                            requests = it.requests,
                            onPop = { key ->
                                onWorksEvent(OnPopRequest(it, key))
                            },
                            onDelete = { key ->
                                onWorksEvent(OnDeleteRequests(it, key))
                            }
                        )
                    }
                    WorksActions(
                        onRun = {
                            if (it.empty) ctx.addRequestToEnqueueToast()
                            else onWorksEvent(OnRunWorks(it))
                        },
                        onDelete = { onWorksEvent(OnDeleteWorks(it)) },
                        onShowRequests = {
                            changeShowRequests(!showRequests)
                        }
                    )
                }
            }
        }

        if (enqueuedWorks.isNotEmpty() && requests.isNotEmpty()) {
            item {
                Divider(
                    color = colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 15.dp),
                )
            }
        }

        items(requests) {
            Spacer(Modifier.height(8.dp))
            ListItem {
                val (showInfo, changeShowInfo) = rememberSaveable {
                    mutableStateOf(true)
                }
                ItemTitle(it.name)
                if (showInfo) {
                    Spacer(Modifier.height(10.dp))
                    RequestInfo(
                        requestType = it.typeName,
                        workerType = it.worker.text,
                        expedited = it.expedited
                    )
                }
                RequestActions(
                    onAdd = { onWorksEvent(OnAddRequest(screenState.selectedWorks, it)) },
                    onInfo = { changeShowInfo(!showInfo) },
                )
            }
        }
    }
}
