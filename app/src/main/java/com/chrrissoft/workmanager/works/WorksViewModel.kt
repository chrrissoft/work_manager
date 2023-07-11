package com.chrrissoft.workmanager.works

import androidx.lifecycle.ViewModel
import com.chrrissoft.workmanager.filterOut
import com.chrrissoft.workmanager.toUUID
import com.chrrissoft.workmanager.works.events.*
import com.chrrissoft.workmanager.works.events.UniqueOneTimeWorksEvents.*
import com.chrrissoft.workmanager.works.events.UniquePeriodicWorksEvent.*
import com.chrrissoft.workmanager.works.events.WorksEvent.*
import com.chrrissoft.workmanager.works.events.WorksScreenStateEvent.*
import com.chrrissoft.workmanager.works.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WorksViewModel @Inject constructor(
    val workManager: WorksManager,
) : ViewModel() {

    private val stateHandler = ScreenStateEventHandler()
    private val worksHandler = WorksEventsHandler()
    private val uniqueOneTimeWorksHandler = UniqueOneTimeWorksEventsHandler()
    private val uniquePeriodicWorksHandler = UniquePeriodicWorksHandler()

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _worksState = MutableStateFlow(WorksState())
    val worksState = _worksState.asStateFlow()


    private val selectedWorks get() = _screenState.value.selectedWorks
    private val selectedOneTimeWorks get() = _screenState.value.selectedOneTime
    private val selectedPeriodicWorks get() = _screenState.value.selectedPeriodic
    private val works get() = _worksState.value.works
    private val operations get() = _worksState.value.operations
    private val uniquesOneTimeWorks get() = _worksState.value.uniquesOneTimeWorks
    private val uniquesPeriodicWorks get() = _worksState.value.uniquesPeriodicWorks

    fun handleState(event: WorksScreenStateEvent) {
        event.handle(stateHandler)
    }

    fun handleWorksEvent(event: AbstractWorksEvents) {
        when (event) {
            is WorksEvent -> handleWorks(event)
            is UniqueOneTimeWorksEvents -> handleOneTimeWorks(event)
            is UniquePeriodicWorksEvent -> handlePeriodicWorks(event)
            else -> throw IllegalStateException()
        }
    }

    private fun handleWorks(event: WorksEvent) {
        event.handle(worksHandler)
    }

    private fun handleOneTimeWorks(event: UniqueOneTimeWorksEvents) {
        event.handle(uniqueOneTimeWorksHandler)
    }

    private fun handlePeriodicWorks(event: UniquePeriodicWorksEvent) {
        event.handle(uniquePeriodicWorksHandler)
    }


    private fun updateWorksState(
        works: List<Works> = _worksState.value.works,
        operations: List<OperationOwn> = _worksState.value.operations,
        uniquesOneTimeWorks: List<UniquesOneTimeWorks> = _worksState.value.uniquesOneTimeWorks,
        uniquesPeriodicWorks: List<UniquesPeriodicWorks> = _worksState.value.uniquesPeriodicWorks,
    ) {
        _worksState.update {
            it.copy(
                works = works,
                operations = operations,
                uniquesOneTimeWorks = uniquesOneTimeWorks,
                uniquesPeriodicWorks = uniquesPeriodicWorks,
            )
        }
    }

    private fun updateState(
        selectedWorks: Works? = _screenState.value.selectedWorks,
        selectedOneTime: UniquesOneTimeWorks? = _screenState.value.selectedOneTime,
        selectedPeriodic: UniquesPeriodicWorks? = _screenState.value.selectedPeriodic,
        page: EnqueueWorksScreenPages = _screenState.value.page,
    ) {
        _screenState.update {
            it.copy(
                page = page,
                selectedWorks = selectedWorks,
                selectedOneTime = selectedOneTime,
                selectedPeriodic = selectedPeriodic,
            )
        }
    }

    inner class ScreenStateEventHandler {
        fun onEvent(event: OnChangeSelectedWorks) {
            updateState(selectedWorks = event.works)
        }

        fun onEvent(event: OnChangePage) {
            updateState(page = event.page)
        }

        fun onEvent(event: OnChangeSelectedOneTimeWorks) {
            updateState(selectedOneTime = event.works)
        }

        fun onEvent(event: OnChangeSelectedPeriodicWorks) {
            updateState(selectedPeriodic = event.works)
        }
    }

    inner class WorksEventsHandler {
        fun onEvent(event: OnRunWorks) {
            if (event.works.requests.isEmpty()) return
            val targetWorks = works.firstOrNull {
                it===event.works
            } ?: return
            val operations = operations.plus(workManager.runWorks(targetWorks))
            val updatedWorks = works.filterOut { it === targetWorks }
            ifTargetWorksWasSelected(targetWorks)
            updateWorksState(works = updatedWorks, operations = operations)
        }

        fun onEvent(event: OnDeleteWorks) {
            val updatedWorks = works.filterOut { it===event.works }
            ifTargetWorksWasSelected(event.works)
            updateWorksState(works = updatedWorks)
        }

        fun onEvent(event: OnDeleteRequests) {
            val targetWorks = works.firstOrNull {
                it===event.works
            } ?: return
            val updatedRequests = targetWorks.requests.filter { it.key!==event.requestsKey }
            val updatedWorks = targetWorks.copy(requests = updatedRequests)
            updateWorksState(works = works.map {
                if (it===event.works) updatedWorks
                else it
            })
            updateState(selectedWorks = updatedWorks)
        }

        fun onEvent(event: OnPopRequest) {
            val targetWorks = works.firstOrNull {
                it===event.works
            } ?: return
            val updatedRequestsStack =
                targetWorks.requests[event.requestsKey]?.dropLast(1) ?: return
            if (updatedRequestsStack.isEmpty()) {
                onEvent(OnDeleteRequests(event.works, event.requestsKey))
                return
            }
            val updatedRequests = targetWorks.requests.toMutableMap()
            updatedRequests[event.requestsKey] = updatedRequestsStack
            val updatedWorks = targetWorks.copy(requests = updatedRequests)
            updateWorksState(works = works.map {
                if (it===event.works) updatedWorks
                else it
            })
            updateState(selectedWorks = updatedWorks)
        }

        fun onEvent(event: OnEnqueueWorks) {
            val newWorks = Works(event.count)
            updateWorksState(works = works.plus(newWorks))
            updateState(selectedWorks = newWorks)
        }

        fun onEvent(event: OnChangeWorksName) {
            val targetWorks = works.firstOrNull {
                it===event.works
            } ?: return
            val updatedWorks = targetWorks.copy(name = event.name)
            updateState(selectedWorks = updatedWorks)
            updateWorksState(works = works.map {
                if (it !== targetWorks) it else updatedWorks
            })
        }

        fun onEvent(event: OnAddRequest) {
            if (event.selectedWork==null) return
            val targetWorks = works.firstOrNull {
                it===event.selectedWork
            } ?: return
            val requestsMap = targetWorks.requests.toMutableMap()
            val requests = requestsMap[event.request.id.toUUID()] ?: emptyList()
            requestsMap[event.request.id.toUUID()] = requests.plus(event.request)
            val updatedWorks = targetWorks.copy(requests = requestsMap)
            updateWorksState(
                works = works.map {
                    if (it!==targetWorks) it
                    else updatedWorks
                }
            )
            updateState(selectedWorks = updatedWorks)
        }
    }

    inner class UniqueOneTimeWorksEventsHandler {
        fun onEvent(event: OnRunUniqueOneTimeWorks) {
            if (event.works.requests.isEmpty()) return
            val targetWorks = uniquesOneTimeWorks.firstOrNull {
                it===event.works
            } ?: return
            val operations = operations.plus(workManager.runUniqueOneTime(targetWorks))
            val updatedWorks = uniquesOneTimeWorks.filterOut { it===event.works }
            updateWorksState(uniquesOneTimeWorks = updatedWorks, operations = operations)
            ifTargetOneTimeWorksWasSelected(targetWorks)
        }

        fun onEvent(event: OnDeleteOneTimeWorks) {
            val updatedWorks = uniquesOneTimeWorks.filterOut { it===event.works }
            updateWorksState(uniquesOneTimeWorks = updatedWorks)
            ifTargetOneTimeWorksWasSelected(event.works)
        }

        fun onEvent(event: OnDeleteOneTimeRequests) {
            val targetWorks = uniquesOneTimeWorks.firstOrNull {
                it===event.works
            } ?: return
            val updatedRequests = targetWorks.requests.filter { it.key!==event.requestsKey }
            val updatedWorks = targetWorks.copy(requests = updatedRequests)
            updateWorksState(uniquesOneTimeWorks = uniquesOneTimeWorks.map {
                if (it!==event.works) it else updatedWorks
            })
            updateState(selectedOneTime = updatedWorks)
        }

        fun onEvent(event: OnPopOneTimeRequest) {
            val targetWorks = uniquesOneTimeWorks.firstOrNull {
                it===event.works
            } ?: return
            val updatedRequestsStack =
                targetWorks.requests[event.requestsKey]?.dropLast(1) ?: return
            if (updatedRequestsStack.isEmpty()) {
                onEvent(OnDeleteOneTimeRequests(event.works, event.requestsKey))
                return
            }
            val updatedRequests = targetWorks.requests.toMutableMap()
            updatedRequests[event.requestsKey] = updatedRequestsStack
            val updatedWorks = targetWorks.copy(requests = updatedRequests)
            updateWorksState(uniquesOneTimeWorks = uniquesOneTimeWorks.map {
                if (it!==event.works) it else updatedWorks
            })
            updateState(selectedOneTime = updatedWorks)
        }

        fun onEvent(event: OnEnqueueUniqueOneTimeWorks) {
            val newWorks = UniquesOneTimeWorks(event.count)
            updateWorksState(uniquesOneTimeWorks = uniquesOneTimeWorks.plus(newWorks))
            updateState(selectedOneTime = newWorks)
        }

        fun onEvent(event: OnAddOneTimeWorks) {
            if (event.selectedWorks==null) return
            val targetWorks = uniquesOneTimeWorks.firstOrNull {
                it===event.selectedWorks
            } ?: return
            val requestsMap = targetWorks.requests.toMutableMap()
            val requests = requestsMap[event.request.id.toUUID()] ?: emptyList()
            requestsMap[event.request.id.toUUID()] = requests.plus(event.request)
            val updatedWorks = targetWorks.copy(requests = requestsMap)
            updateWorksState(
                uniquesOneTimeWorks = uniquesOneTimeWorks.map {
                    if (it!==targetWorks) it
                    else updatedWorks
                }
            )
            updateState(selectedOneTime = updatedWorks)
        }

        fun onEvent(event: OnChangeOneTimeName) {
            val targetWorks = uniquesOneTimeWorks.firstOrNull {
                it===event.works
            } ?: return
            val updatedWorks = targetWorks.copy(name = event.name)
            updateState(selectedOneTime = updatedWorks)
            updateWorksState(uniquesOneTimeWorks = uniquesOneTimeWorks.map {
                if (it !== targetWorks) it else updatedWorks
            })
        }
    }

    inner class UniquePeriodicWorksHandler {
        fun onEvent(event: OnRunUniquePeriodicWorks) {
            if (event.works.request == null) return
            val targetWorks = uniquesPeriodicWorks.firstOrNull {
                it===event.works
            } ?: return
            val operations = operations.plus(workManager.runUniquePeriodic(targetWorks))
            val updatedWorks = uniquesPeriodicWorks.filterOut { it===event.works }
            if (selectedPeriodicWorks==targetWorks) {
                val newSelected = uniquesPeriodicWorks.firstOrNull() ?: return
                stateHandler.onEvent(OnChangeSelectedPeriodicWorks(newSelected))
            }
            updateWorksState(uniquesPeriodicWorks = updatedWorks, operations = operations)
            ifTargetPeriodicWorksWasSelected(targetWorks)
        }

        fun onEvent(event: OnDeletePeriodicWorks) {
            val updatedWorks = uniquesPeriodicWorks.filterOut { it===event.works }
            updateWorksState(uniquesPeriodicWorks = updatedWorks)
            ifTargetPeriodicWorksWasSelected(event.works)
        }

        fun onEvent(event: OnEnqueueUniquePeriodicWork) {
            val newWorks = UniquesPeriodicWorks(event.count)
            updateWorksState(uniquesPeriodicWorks = uniquesPeriodicWorks.plus(newWorks))
            updateState(selectedPeriodic = newWorks)
        }

        fun onEvent(event: OnAddPeriodicRequest) {
            if (event.selectedWorks==null) return
            val targetWorks = uniquesPeriodicWorks.firstOrNull {
                it===event.selectedWorks
            } ?: return
            val updatedWorks = targetWorks.copy(request = event.request)
            updateWorksState(
                uniquesPeriodicWorks = uniquesPeriodicWorks.map {
                    if (it!==targetWorks) it
                    else updatedWorks
                }
            )
            updateState(selectedPeriodic = updatedWorks)
        }

        fun onEvent(event: OnChangePeriodicName) {
            val targetWorks = uniquesPeriodicWorks.firstOrNull {
                it===event.works
            } ?: return
            val updatedWorks = targetWorks.copy(name = event.name)
            updateState(selectedPeriodic = updatedWorks)
            updateWorksState(uniquesPeriodicWorks = uniquesPeriodicWorks.map {
                if (it !== targetWorks) it else updatedWorks
            })
        }
    }

    fun ifTargetWorksWasSelected(
        target: Works,
        replaceWith: Works? = null,
    ) {
        if (selectedWorks===target) {
            val first = works.firstOrNull()
            val newSelected = replaceWith ?: first ?: return
            stateHandler.onEvent(OnChangeSelectedWorks(newSelected))
        }
    }

    fun ifTargetOneTimeWorksWasSelected(
        target: UniquesOneTimeWorks,
        replaceWith: UniquesOneTimeWorks? = null,
    ) {
        if (selectedOneTimeWorks===target) {
            val first = uniquesOneTimeWorks.firstOrNull()
            val newSelected = replaceWith ?: first ?: return
            stateHandler.onEvent(OnChangeSelectedOneTimeWorks(newSelected))
        }
    }

    fun ifTargetPeriodicWorksWasSelected(
        target: UniquesPeriodicWorks,
        replaceWith: UniquesPeriodicWorks? = null,
    ) {
        if (selectedPeriodicWorks===target) {
            val first = uniquesPeriodicWorks.firstOrNull()
            val newSelected = replaceWith ?: first ?: return
            stateHandler.onEvent(OnChangeSelectedPeriodicWorks(newSelected))
        }
    }
}
