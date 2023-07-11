package com.chrrissoft.workmanager.info.pages

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.workmanager.filterOut
import com.chrrissoft.workmanager.info.InfoEvent.OnInfoById
import com.chrrissoft.workmanager.info.InfoState
import com.chrrissoft.workmanager.info.components.InfoItem
import com.chrrissoft.workmanager.info.components.SelectableForObserve
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog
import java.util.*

//private const val TAG = "InfosByIdPage"

@Composable
fun InfosByIdPage(
    allIds: Set<UUID>,
    showIds: Boolean,
    onChangeShowIds: (Boolean) -> Unit,
    infos: InfoState.InfoByIds,
    onRequestForInfo: (OnInfoById) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (observedIds, onChangeObservedIds) = remember {
        mutableStateOf(setOf<UUID>())
    }

    if (showIds) {
        AlertDialog(
            title = {
                TitleAlertDialog(text = "Add Ids To Observe")
            },
            content = {
                allIds.forEach { id ->
                    SelectableForObserve(
                        text = id.hashCode().toString(),
                        checked = observedIds.contains(id),
                        onClick = { selected ->
                            if (selected) onChangeObservedIds(observedIds.plus(id))
                            else onChangeObservedIds(observedIds.filterOut { it==id })
                        },
                    )
                }
            },
            onConfirm = {
                onRequestForInfo(OnInfoById(observedIds))
                onChangeShowIds(false)
            },
            onDismissRequest = {
                onChangeShowIds(false)
            },
        )
    }

    LazyColumn(modifier) {
        items(infos.infos.toList().map { it.second }) {
            Spacer(modifier = Modifier.height(5.dp))
            InfoItem(info = it)
        }
    }
}
