package com.chrrissoft.workmanager.works.ui.pages

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.request.state.PeriodicWorkRequestOwn
import com.chrrissoft.workmanager.ui.components.ItemTitle
import com.chrrissoft.workmanager.ui.components.ListItem
import com.chrrissoft.workmanager.ui.components.RequestInfo
import com.chrrissoft.workmanager.works.addRequestToEnqueueToast
import com.chrrissoft.workmanager.works.events.UniquePeriodicWorksEvent
import com.chrrissoft.workmanager.works.events.UniquePeriodicWorksEvent.*
import com.chrrissoft.workmanager.works.events.WorksScreenStateEvent
import com.chrrissoft.workmanager.works.events.WorksScreenStateEvent.OnChangeSelectedPeriodicWorks
import com.chrrissoft.workmanager.works.state.ScreenState
import com.chrrissoft.workmanager.works.state.UniquesPeriodicWorks
import com.chrrissoft.workmanager.works.ui.RequestActions
import com.chrrissoft.workmanager.works.ui.WorksActions
import com.chrrissoft.workmanager.works.ui.WorksTitleInput

@Composable
fun UniquePeriodicWorksPage(
    requests: List<PeriodicWorkRequestOwn>,
    screenState: ScreenState,
    onStateEvent: (WorksScreenStateEvent) -> Unit,
    enqueuedWorks: List<UniquesPeriodicWorks>,
    onWorksEvent: (UniquePeriodicWorksEvent) -> Unit,
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
                    selected = it===screenState.selectedPeriodic,
                    onClick = {
                        onStateEvent(OnChangeSelectedPeriodicWorks(it))
                    }
                ) {
                    WorksTitleInput(
                        name = it.name,
                        onChangeName = { name ->
                            onWorksEvent(OnChangePeriodicName(it, name))
                        }
                    )
                    if (showRequests) {
                        Spacer(Modifier.height(10.dp))
                        ItemTitle(text = "${it.request?.name}")
                    }
                    WorksActions(
                        onRun = {
                            if (it.isNull) ctx.addRequestToEnqueueToast()
                            else onWorksEvent(OnRunUniquePeriodicWorks(it))
                        },
                        onDelete = { onWorksEvent(OnDeletePeriodicWorks(it)) },
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
                    color = MaterialTheme.colorScheme.primary,
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
                    onAdd = {
                        onWorksEvent(
                            OnAddPeriodicRequest(
                                screenState.selectedPeriodic,
                                it
                            )
                        )
                    },
                    onInfo = { changeShowInfo(!showInfo) },
                )
            }
        }
    }
}
