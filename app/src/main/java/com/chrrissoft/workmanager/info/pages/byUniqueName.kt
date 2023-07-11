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
import com.chrrissoft.workmanager.info.InfoEvent.OnInfoByUniqueName
import com.chrrissoft.workmanager.info.InfoState.InfoByUniqueNames
import com.chrrissoft.workmanager.info.components.InfoItem
import com.chrrissoft.workmanager.info.components.SelectableForObserve
import com.chrrissoft.workmanager.ui.components.AlertDialog
import com.chrrissoft.workmanager.ui.components.TitleAlertDialog

//private const val TAG = "InfosByUniqueNamePage"

@Composable
fun InfosByUniqueNamePage(
    names: Set<String>,
    showNames: Boolean,
    onChangeShowNames: (Boolean) -> Unit,
    onRequest: (OnInfoByUniqueName) -> Unit,
    infos: InfoByUniqueNames,
    modifier: Modifier = Modifier,
) {
    val (observedTags, onChangeObservedTags) = remember {
        mutableStateOf(setOf<String>())
    }

    if (showNames) {
        AlertDialog(
            title = {
                TitleAlertDialog(text = "Add Unique Names To Observe")
            },
            content = {
                names.forEach { name ->
                    SelectableForObserve(
                        text = name,
                        checked = observedTags.contains(name),
                    ) { selected ->
                        if (selected) onChangeObservedTags(observedTags.plus(name))
                        else onChangeObservedTags(observedTags.filterOut { it==name })
                    }
                }
            },
            onConfirm = {
                onRequest(OnInfoByUniqueName(observedTags))
                onChangeShowNames(false)
            },
            onDismissRequest = {
                onChangeShowNames(false)
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
