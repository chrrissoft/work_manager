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
import com.chrrissoft.workmanager.info.InfoEvent.OnInfoByTag
import com.chrrissoft.workmanager.info.InfoState
import com.chrrissoft.workmanager.info.components.InfoItem
import com.chrrissoft.workmanager.info.components.SelectableForObserve
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog

@Composable
fun InfosByTagPage(
    tags: Set<String>,
    showTags: Boolean,
    onChangeShowTags: (Boolean) -> Unit,
    onRequest: (OnInfoByTag) -> Unit,
    infos: InfoState.InfoByTags,
    modifier: Modifier = Modifier,
) {

    val (observedTags, changeObservedTags) = remember {
        mutableStateOf(setOf<String>())
    }

    if (showTags) {
        AlertDialog(
            title = {
                TitleAlertDialog(text = "Add Tags To Observe")
            },
            content = {
                tags.forEach { tag ->
                    SelectableForObserve(
                        text = tag,
                        checked = observedTags.contains(tag),
                    ) { selected ->
                        if (selected) changeObservedTags(observedTags.plus(tag))
                        else changeObservedTags(observedTags.filterOut { it==tag })
                    }
                }
            },
            onConfirm = {
                onRequest(OnInfoByTag(observedTags))
                onChangeShowTags(false)
            },
            onDismissRequest = {
                onChangeShowTags(false)
            },
        )
    }

    LazyColumn(modifier) {
        items(infos.infos.map { it.key }) { tag ->
            infos.infos[tag]?.forEach {
                Spacer(modifier = Modifier.height(5.dp))
                InfoItem(info = it)
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}
