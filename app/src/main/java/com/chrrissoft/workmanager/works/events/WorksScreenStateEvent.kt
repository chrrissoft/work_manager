package com.chrrissoft.workmanager.works.events

import com.chrrissoft.workmanager.works.WorksViewModel.ScreenStateEventHandler
import com.chrrissoft.workmanager.works.state.EnqueueWorksScreenPages
import com.chrrissoft.workmanager.works.state.UniquesOneTimeWorks
import com.chrrissoft.workmanager.works.state.UniquesPeriodicWorks
import com.chrrissoft.workmanager.works.state.Works

sealed interface WorksScreenStateEvent {
    fun handle(handler: ScreenStateEventHandler) {
        when (this) {
            is OnChangeSelectedWorks -> handler.onEvent(this)
            is OnChangePage -> handler.onEvent(this)
            is OnChangeSelectedOneTimeWorks -> handler.onEvent(this)
            is OnChangeSelectedPeriodicWorks -> handler.onEvent(this)
        }
    }

    data class OnChangeSelectedWorks(val works: Works) : WorksScreenStateEvent
    data class OnChangeSelectedOneTimeWorks(val works: UniquesOneTimeWorks) :
        WorksScreenStateEvent

    data class OnChangeSelectedPeriodicWorks(val works: UniquesPeriodicWorks) :
        WorksScreenStateEvent

    data class OnChangePage(val page: EnqueueWorksScreenPages) : WorksScreenStateEvent
}
