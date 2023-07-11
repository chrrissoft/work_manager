package com.chrrissoft.workmanager.request

import android.util.Log
import androidx.lifecycle.ViewModel
import com.chrrissoft.workmanager.filterOut
import com.chrrissoft.workmanager.request.RequestEvent.*
import com.chrrissoft.workmanager.request.state.*
import com.chrrissoft.workmanager.request.state.RequestScreenPage.Builder
import com.chrrissoft.workmanager.request.state.RequestType.OneTime
import com.chrrissoft.workmanager.request.state.RequestType.Periodic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*


class WorkRequestViewModel : ViewModel() {

    private val handler = EventHandler()

    private val oneTimeWorkRequestCache = OneTimeWorkRequestOwn()
    private val periodicWorkRequestCache = PeriodicWorkRequestOwn()
    private val _state = MutableStateFlow(RequestState())
    val state = _state.asStateFlow()


    fun handleEvent(event: RequestEvent) {
        Log.d(TAG, event.toString())
        if (_state.value.viewMode == ViewMode.View && event !is OnExitViewRequest) return
        event.handle(handler)
    }

    inner class EventHandler {

        fun onEvent(event: OnAddTag) {
            val tags = _state.value.request.tags.plus(event.tag)
            val request = _state.value.request.copyOwn(tags = tags)
            updateState(request)
        }

        fun onEvent(event: OnAddData) {
            val map = _state.value.request.specOwn.inputData.plus(event.data)
            val spec = _state.value.request.specOwn.copy(inputData = map)
            val request = _state.value.request.copyOwn(specOwn = spec)
            updateState(request)
        }

        fun onEvent(event: OnDeleteData) {
            val map = _state.value.request.specOwn.inputData.filter { it.key!=event.key }
            val spec = _state.value.request.specOwn.copy(inputData = map)
            val request = _state.value.request.copyOwn(specOwn = spec)
            updateState(request)
        }

        fun onEvent(event: OnDeleteTag) {
            val tags = _state.value.request.tags.filterOut { it==event.tag }.toSet()
            val request = _state.value.request.copyOwn(tags = tags)
            updateState(request = request)
        }

        fun onEvent(event: OnInitialDelay) {
            val specOwn = _state.value.request.specOwn.copy(
                initialDelaySeconds = event.seconds,
            )
            val request = _state.value.request.copyOwn(specOwn = specOwn)
            updateState(request = request)
        }

        fun onEvent(event: OnInputMerger) {
            val request = _state.value.request.asOneTimeOwn().copy(inputMerger = event.merger)
            updateState(request = request)
        }

        fun onEvent(event: OnBackoffCriteria) {
            val spec = _state.value.request.specOwn.copy(
                backoffPolicy = event.policy,
                backoffDelayMinutes = event.minutes,
            )
            val request = _state.value.request.copyOwn(specOwn = spec)
            updateState(request = request)
        }

        fun onEvent(event: OnConstraints) {
            val spec = _state.value.request
                .specOwn.copy(constraints = event.constraints)
            val request = _state.value.request.copyOwn(specOwn = spec)
            updateState(request = request)
        }

        fun onEvent(event: OnEnableExpedited) {
            val request = _state.value.request.copyOwn(expedited = event.enable)
            updateState(request = request)
        }

        fun onEvent(event: OnEnableFlexInterval) {
            val request = _state.value.request.asPeriodicOwn().copy(flexInterval = event.enable)
            updateState(request = request)
        }

        fun onEvent(event: OnWorker) {
            val request = _state.value.request.copyOwn(worker = event.worker)
            updateState(request = request)
        }

        fun onEvent(event: OnRequestType) {
            val request = when (event.type) {
                OneTime -> oneTimeWorkRequestCache
                Periodic -> periodicWorkRequestCache
            }
            updateState(request = request)
        }

        fun onEvent(event: OnPeriodicInterval) {
            val spec = _state.value.request.specOwn.copy(
                intervalMinutes = event.minutes,
            )
            val request = _state.value.request.copyOwn(specOwn = spec)
            updateState(request)
        }

        fun onEvent(event: OnAddRequest) {
            val requests = when (_state.value.viewMode) {
                ViewMode.Editing -> {
                    updateState(viewMode = ViewMode.Creating)
                    _state.value.requests.map {
                        if (it.id == event.request.id) event.request
                        else it
                    }
                }
                ViewMode.Creating -> {
                    _state.value.requests.plus(event.request)
                }
                ViewMode.View -> throw IllegalStateException()
            }
            Log.d(TAG, _state.value.request.id)
            updateState(
                requests = requests,
                request = _state.value.request.copyOwn(id = UUID.randomUUID().toString())
            )
            Log.d(TAG, _state.value.request.id)
        }

        fun onEvent(event: OnOutOfQuotaPolicy) {
            val specs = _state.value.request.specOwn.copy(outOfQuotaPolicy = event.policy)
            val request = _state.value.request.copyOwn(specOwn = specs)
            updateState(request = request)
        }

        fun onEvent(event: OnFlexInterval) {
            val specs = _state.value.request.specOwn.copy(flexMinutes = event.minutes)
            val request = _state.value.request.copyOwn(specOwn = specs)
            updateState(request = request)
        }

        fun onEvent(event: OnChangeRequestName) {
            val request = _state.value.request.copyOwn(name = event.name)
            updateState(request = request)
        }

        fun onEvent(event: OnDeleteRequest) {
            val requests = _state.value.requests.filterOut { it.id == event.id }
            updateState(requests = requests)
        }

        fun onEvent(event: OnEditRequest) {
            val request = _state.value.requests.find { it.id == event.id } ?: return
            updateState(request = request, page = Builder, viewMode = ViewMode.Editing)
        }

        fun onEvent(event: OnViewRequest) {
            val request = _state.value.requests.find { it.id == event.id } ?: return
            updateState(request = request, viewMode = ViewMode.View, page = Builder)
        }

        fun onExistViewMode() {
            updateState(
                page = Builder,
                viewMode = ViewMode.Creating,
                request = _state.value.request.copyOwn(UUID.randomUUID().toString()),
            )
        }

        fun onEvent(event: OnChangeRequestPage) {
            updateState(page = event.page)
        }

    }

    private fun updateState(
        request: WorkRequestOwn = _state.value.request,
        requests: List<WorkRequestOwn> = _state.value.requests,
        page: RequestScreenPage = _state.value.page,
        viewMode: ViewMode = _state.value.viewMode,
    ) {
        _state.update {
            it.copy(
                page = page,
                request = request,
                requests = requests,
                viewMode = viewMode,
            )
        }
    }

    private companion object {
        private const val TAG = "WorkRequestViewModel"
    }
}
