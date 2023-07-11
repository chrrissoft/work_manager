package com.chrrissoft.workmanager.works.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material.icons.rounded.WorkHistory
import androidx.compose.ui.graphics.vector.ImageVector

enum class EnqueueWorksScreenPages(
    val text: String, val icon: ImageVector,
) {
    Works("Works", Icons.Rounded.Work),
    UniqueOneTimeWorks("One time", Icons.Rounded.Work),
    UniquePeriodicWorks("Periodic", Icons.Rounded.WorkHistory),
    Operations("Operations", Icons.Rounded.List),
}
