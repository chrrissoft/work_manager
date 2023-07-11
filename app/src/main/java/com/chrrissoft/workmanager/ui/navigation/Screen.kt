package com.chrrissoft.workmanager.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Plumbing
import androidx.compose.material.icons.rounded.WorkHistory
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface Screen {
    val name: String
    val route: String
    val icon: ImageVector

    object WorkInfoScreen : Screen {
        override val name: String = "Work Info"
        override val route: String = "work_info"
        override val icon: ImageVector = Icons.Rounded.Info
    }
    object WorkManagerScreen : Screen {
        override val route: String = "work_manager"
        override val name: String = "Work Manager"
        override val icon: ImageVector = Icons.Rounded.WorkHistory
    }
    object RequestBuilderScreen : Screen {
        override val name: String = "Request Builder"
        override val route: String = "request_builder"
        override val icon: ImageVector = Icons.Rounded.Plumbing
    }

}
