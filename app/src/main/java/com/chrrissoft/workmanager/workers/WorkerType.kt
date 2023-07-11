package com.chrrissoft.workmanager.workers

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import java.util.concurrent.TimeUnit

@Suppress("propertyName")
enum class WorkerType(val text: String) {
    Worker("Worker"), CoroutineWorker("Coroutine");

    val oneTimeBuilder: OneTimeWorkRequest.Builder
        get() = run {
            when (this) {
                Worker -> OneTimeWorkRequestBuilder<MyWorker>()
                CoroutineWorker -> OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            }
        }


    fun periodicBuilder(
        interval: Long, intervalUnit: TimeUnit,
    ): PeriodicWorkRequest.Builder {
        return when (this) {
            Worker -> PeriodicWorkRequestBuilder<MyWorker>(interval, intervalUnit)
            CoroutineWorker -> PeriodicWorkRequestBuilder<MyCoroutineWorker>(interval, intervalUnit)
        }
    }

    fun periodicFlexBuilder(
        interval: Long, intervalUnit: TimeUnit, flex: Long, flexUnit: TimeUnit,
    ): PeriodicWorkRequest.Builder {
        return when (this) {
            Worker -> PeriodicWorkRequestBuilder<MyWorker>(interval, intervalUnit, flex, flexUnit)
            CoroutineWorker -> PeriodicWorkRequestBuilder<MyCoroutineWorker>(interval,
                intervalUnit,
                flex,
                flexUnit)
        }
    }

}
