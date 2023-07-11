package com.chrrissoft.workmanager.request.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.ui.graphics.vector.ImageVector

enum class RequestScreenPage(val icon: ImageVector, val text: String) {
    Builder(Icons.Rounded.Plumbing, "Builder"), Requests(Icons.Rounded.List, "List")
}
