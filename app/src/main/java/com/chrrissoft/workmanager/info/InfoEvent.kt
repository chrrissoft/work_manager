package com.chrrissoft.workmanager.info

import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkQuery
import java.util.*

sealed interface InfoEvent {

    fun handle(handler: InfoViewModel.EventHandler) {
        when (this) {
            is OnInfoById -> handler.onEvent(this)
            is OnInfoByQuery -> handler.onEvent(this)
            is OnInfoByTag -> handler.onEvent(this)
            is OnInfoByUniqueName -> handler.onEvent(this)
            is OnChangePage -> handler.onEvent(this)
            is OnInitializeObservableWorks -> handler.onEvent(this)
        }
    }

    data class OnInfoById(val ids: Set<UUID>) : InfoEvent
    data class OnInfoByTag(val tags: Set<String>) : InfoEvent
    data class OnInfoByQuery(val query: Set<WorkQuery>) : InfoEvent
    data class OnInfoByUniqueName(val names: Set<String>) : InfoEvent

    data class OnChangePage(val page: InfoState.InfoScreenPage) : InfoEvent
    data class OnInitializeObservableWorks(val owner: LifecycleOwner) : InfoEvent
}
