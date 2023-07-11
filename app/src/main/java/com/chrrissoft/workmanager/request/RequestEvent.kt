package com.chrrissoft.workmanager.request

import androidx.work.BackoffPolicy
import androidx.work.OutOfQuotaPolicy
import com.chrrissoft.workmanager.request.state.*
import com.chrrissoft.workmanager.workers.WorkerType

sealed interface RequestEvent {
    fun handle(handler: WorkRequestViewModel.EventHandler) {
        when (this) {
            is OnBackoffCriteria -> handler.onEvent(this)
            is OnAddData -> handler.onEvent(this)
            is OnDeleteData -> handler.onEvent(this)
            is OnWorker -> handler.onEvent(this)
            is OnConstraints -> handler.onEvent(this)
            is OnEnableExpedited -> handler.onEvent(this)
            is OnInitialDelay -> handler.onEvent(this)
            is OnInputMerger -> handler.onEvent(this)
            is OnAddTag -> handler.onEvent(this)
            is OnDeleteTag -> handler.onEvent(this)
            is OnRequestType -> handler.onEvent(this)
            is OnPeriodicInterval -> handler.onEvent(this)
            is OnAddRequest -> handler.onEvent(this)
            is OnOutOfQuotaPolicy -> handler.onEvent(this)
            is OnEnableFlexInterval -> handler.onEvent(this)
            is OnFlexInterval -> handler.onEvent(this)
            is OnChangeRequestName -> handler.onEvent(this)
            is OnDeleteRequest -> handler.onEvent(this)
            is OnEditRequest -> handler.onEvent(this)
            is OnChangeRequestPage -> handler.onEvent(this)
            is OnViewRequest -> handler.onEvent(this)
            is OnExitViewRequest -> handler.onExistViewMode()
        }
    }

    data class OnAddTag(val tag: String) : RequestEvent
    data class OnDeleteTag(val tag: String) : RequestEvent

    data class OnDeleteData(val key: String) : RequestEvent
    data class OnAddData(val data: Pair<String, String>) : RequestEvent

    data class OnFlexInterval(val minutes: Float) : RequestEvent
    data class OnEnableFlexInterval(val enable: Boolean) : RequestEvent

    data class OnEnableExpedited(val enable: Boolean) : RequestEvent
    data class OnOutOfQuotaPolicy(val policy: OutOfQuotaPolicy) : RequestEvent

    data class OnInitialDelay(val seconds: Float) : RequestEvent
    data class OnConstraints(val constraints: ConstraintsOwn) : RequestEvent
    data class OnInputMerger(val merger: InputMergerOwn) : RequestEvent
    data class OnBackoffCriteria(val policy: BackoffPolicy, val minutes: Float) : RequestEvent

    data class OnWorker(val worker: WorkerType) : RequestEvent
    data class OnPeriodicInterval(val minutes: Float) : RequestEvent
    data class OnRequestType(val type: RequestType) : RequestEvent

    data class OnAddRequest(val request: WorkRequestOwn) : RequestEvent
    data class OnChangeRequestName(val name: String) : RequestEvent

    data class OnEditRequest(val id: String) : RequestEvent
    data class OnDeleteRequest(val id: String) : RequestEvent
    data class OnViewRequest(val id: String) : RequestEvent
    object OnExitViewRequest : RequestEvent

    data class OnChangeRequestPage(val page: RequestScreenPage) : RequestEvent
}
