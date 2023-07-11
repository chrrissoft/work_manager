package com.chrrissoft.workmanager.works.events

import com.chrrissoft.workmanager.request.state.OneTimeWorkRequestOwn
import com.chrrissoft.workmanager.works.WorksViewModel
import com.chrrissoft.workmanager.works.state.UniquesOneTimeWorks
import java.util.*

sealed interface UniqueOneTimeWorksEvents : AbstractWorksEvents {
    fun handle(handler: WorksViewModel.UniqueOneTimeWorksEventsHandler) {
        when (this) {
            is OnRunUniqueOneTimeWorks -> handler.onEvent(this)
            is OnDeleteOneTimeWorks -> handler.onEvent(this)
            is OnDeleteOneTimeRequests -> handler.onEvent(this)
            is OnPopOneTimeRequest -> handler.onEvent(this)
            is OnEnqueueUniqueOneTimeWorks -> handler.onEvent(this)
            is OnAddOneTimeWorks -> handler.onEvent(this)
            is OnChangeOneTimeName -> handler.onEvent(this)
        }
    }

    data class OnRunUniqueOneTimeWorks(val works: UniquesOneTimeWorks) : UniqueOneTimeWorksEvents

    data class OnDeleteOneTimeWorks(val works: UniquesOneTimeWorks) : UniqueOneTimeWorksEvents

    data class OnDeleteOneTimeRequests(
        val works: UniquesOneTimeWorks,
        val requestsKey: UUID
    ) : UniqueOneTimeWorksEvents

    data class OnPopOneTimeRequest(
        val works: UniquesOneTimeWorks,
        val requestsKey: UUID,
    ) : UniqueOneTimeWorksEvents

    data class OnEnqueueUniqueOneTimeWorks(val count: Int) : UniqueOneTimeWorksEvents

    data class OnAddOneTimeWorks(
        val selectedWorks: UniquesOneTimeWorks?,
        val request: OneTimeWorkRequestOwn,
    ) : UniqueOneTimeWorksEvents

    data class OnChangeOneTimeName(val works: UniquesOneTimeWorks, val name: String) : UniqueOneTimeWorksEvents
}
