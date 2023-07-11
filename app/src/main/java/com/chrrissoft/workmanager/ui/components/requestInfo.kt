package com.chrrissoft.workmanager.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestInfo(
    requestType: String,
    workerType: String,
    expedited: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val info = buildList {
            add(Pair(requestType, Icons.Rounded.Timer))
            add(Pair(workerType, Icons.Rounded.Info))
            add(Pair(expedited.toString(), Icons.Rounded.Speed))
        }
        info.forEachIndexed { index, pair ->
            MyInputChip(
                label = { Text(text = pair.first) },
                modifier = Modifier
                    .weight(50f)
                    .padding(0.dp),
                leadingIcon = {
                    Icon(pair.second, null)
                }
            )
            if (index!=info.lastIndex) {
                Box(Modifier.weight(3f))
            }
        }
    }
}
