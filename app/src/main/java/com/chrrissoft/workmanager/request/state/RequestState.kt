package com.chrrissoft.workmanager.request.state


data class RequestState(
    val requests: List<WorkRequestOwn> = emptyList(),
    val request: WorkRequestOwn = OneTimeWorkRequestOwn(),
    val page: RequestScreenPage = RequestScreenPage.Builder,
    val viewMode: ViewMode = ViewMode.Creating,
)
