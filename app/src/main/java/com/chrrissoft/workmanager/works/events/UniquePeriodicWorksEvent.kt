package com.chrrissoft.workmanager.works.events

import com.chrrissoft.workmanager.request.state.PeriodicWorkRequestOwn
import com.chrrissoft.workmanager.works.WorksViewModel
import com.chrrissoft.workmanager.works.state.UniquesPeriodicWorks

sealed interface UniquePeriodicWorksEvent : AbstractWorksEvents {
    fun handle(handler: WorksViewModel.UniquePeriodicWorksHandler) {
        when (this) {
            is OnRunUniquePeriodicWorks -> handler.onEvent(this)
            is OnDeletePeriodicWorks -> handler.onEvent(this)
            is OnEnqueueUniquePeriodicWork -> handler.onEvent(this)
            is OnAddPeriodicRequest -> handler.onEvent(this)
            is OnChangePeriodicName -> handler.onEvent(this)
        }
    }


    data class OnRunUniquePeriodicWorks(val works: UniquesPeriodicWorks) : UniquePeriodicWorksEvent

    data class OnDeletePeriodicWorks(val works: UniquesPeriodicWorks) : UniquePeriodicWorksEvent

    data class OnEnqueueUniquePeriodicWork(val count: Int) : UniquePeriodicWorksEvent

    data class OnAddPeriodicRequest(
        val selectedWorks: UniquesPeriodicWorks?,
        val request: PeriodicWorkRequestOwn,
    ) : UniquePeriodicWorksEvent

    data class OnChangePeriodicName(
        val works: UniquesPeriodicWorks,
        val name: String
    ) : UniquePeriodicWorksEvent
}
