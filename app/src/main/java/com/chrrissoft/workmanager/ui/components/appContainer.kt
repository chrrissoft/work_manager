package com.chrrissoft.workmanager.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.workmanager.ui.theme.WorkManagerTheme


@Composable
fun AppUI(content: @Composable () -> Unit) {
    WorkManagerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) { content() }
    }
}
