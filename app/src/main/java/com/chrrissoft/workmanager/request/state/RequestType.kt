package com.chrrissoft.workmanager.request.state

enum class RequestType(val text: String) {
    OneTime("One time"), Periodic("Periodic");

    companion object {
        fun getFromRequestOnw(request: WorkRequestOwn) : RequestType {
            return when (request) {
                is OneTimeWorkRequestOwn -> OneTime
                is PeriodicWorkRequestOwn -> Periodic
            }
        }
    }
}
