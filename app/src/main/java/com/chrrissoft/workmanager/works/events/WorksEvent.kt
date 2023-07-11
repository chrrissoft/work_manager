package com.chrrissoft.workmanager.works.events

import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import com.chrrissoft.workmanager.works.WorksViewModel
import com.chrrissoft.workmanager.works.state.Works
import java.util.*

sealed interface WorksEvent : AbstractWorksEvents {
    fun handle(handler: WorksViewModel.WorksEventsHandler) {
        when (this) {
            is OnChangeWorksName -> handler.onEvent(this)
            is OnRunWorks -> handler.onEvent(this)
            is OnDeleteWorks -> handler.onEvent(this)
            is OnDeleteRequests -> handler.onEvent(this)
            is OnPopRequest -> handler.onEvent(this)
            is OnEnqueueWorks -> handler.onEvent(this)
            is OnAddRequest -> handler.onEvent(this)
        }
    }

    data class OnChangeWorksName(val works: Works, val name: String) : WorksEvent
    data class OnRunWorks(val works: Works) : WorksEvent
    data class OnDeleteWorks(val works: Works) : WorksEvent
    data class OnDeleteRequests(val works: Works, val requestsKey: UUID) : WorksEvent
    data class OnPopRequest(val works: Works, val requestsKey: UUID) : WorksEvent
    data class OnEnqueueWorks(val count: Int) : WorksEvent
    data class OnAddRequest(val selectedWork: Works?, val request: WorkRequestOwn) : WorksEvent
}
