package com.chrrissoft.workmanager.info

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.Pin
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.work.WorkInfo
import androidx.work.WorkQuery
import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import java.util.*

data class InfoState(
    val infosById: InfoByIds = InfoByIds(),
    val infosByTag: InfoByTags = InfoByTags(),
    val infosByQuery: InfoByQuery = InfoByQuery(),
    val infoByUniqueNames: InfoByUniqueNames = InfoByUniqueNames(),
    val observablesWorks: ObservablesWorks = ObservablesWorks(),
    val page: InfoScreenPage = InfoScreenPage.ByID,
) {
    data class InfoByIds(val infos: Map<UUID, WorkInfoOwn> = mapOf())

    data class InfoByTags(val infos : Map<String, List<WorkInfoOwn>> = mapOf())

    data class InfoByQuery(val infos : Map<WorkQuery, List<WorkInfoOwn>> = mapOf())

    data class InfoByUniqueNames(val infos : Map<String, List<WorkInfoOwn>> = mapOf())

    data class ObservablesWorks(
        val ids: Set<UUID> = emptySet(),
        val tags: Set<String> = emptySet(),
        val uniqueName: Set<String> = emptySet(),
    )

    enum class InfoScreenPage(val icon: ImageVector, val text: String) {
        ByID(Icons.Rounded.Pin, text = "By id"),
        ByTag(Icons.Rounded.Sell, text = "By tag"),
        ByUniqueName(Icons.Rounded.Badge, text = "By name"),
        ByQuery(Icons.Rounded.QueryStats, text = "By query"),
    }

    data class WorkInfoOwn(val info: WorkInfo, val request: WorkRequestOwn)
}
