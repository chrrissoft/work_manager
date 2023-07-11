package com.chrrissoft.workmanager.info.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.Data
import com.chrrissoft.workmanager.info.InfoState
import com.chrrissoft.workmanager.info.filterOutTags
import com.chrrissoft.workmanager.ui.components.ItemTitle
import com.chrrissoft.workmanager.ui.components.ListItem
import com.chrrissoft.workmanager.ui.components.MyInputChip
import com.chrrissoft.workmanager.workers.COMMON_OUTPUT_DATA
import com.chrrissoft.workmanager.workers.COMMON_PROGRESS_DATA

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoItem(
    info: InfoState.WorkInfoOwn,
    onClick: () -> Unit = {}
) {
    ListItem(
        onClick = { onClick() }
    ) {
        ItemTitle(text = info.request.name)

        Spacer(modifier = Modifier.height(5.dp))
        ItemTitle(text = "state: ${info.info.state.name.lowercase()}  -  progress: ${info.info.progress.getProgress()}")

        if (info.info.tags.filterOutTags().isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                Text(text = "tags: ")
                info.info.tags.filterOutTags().forEach {
                    MyInputChip(
                        label = { Text(text = it) },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        if (info.info.state.isFinished) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                Text(text = "output data: ")
                MyInputChip(
                    label = { Text(text = " ${info.info.outputData.getOutputData()}") },
                )
            }
        }
    }
}

fun Data.getOutputData(): String {
    return getString(COMMON_OUTPUT_DATA)!!
}

fun Data.getProgress(): String {
    val progress = "${getInt(COMMON_PROGRESS_DATA, 1000)}"
    return if (progress == "1000") "no running" else progress
}
