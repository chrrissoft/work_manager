package com.chrrissoft.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.chrrissoft.workmanager.workers.MyCoroutineWorker
import com.chrrissoft.workmanager.workers.MyWorker
import javax.inject.Inject

class MyWorkerFactory @Inject constructor() : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            MyWorker.NAME -> {
                MyWorker(
                    ctx = appContext,
                    parameters = workerParameters
                )
            }
            MyCoroutineWorker.NAME -> {
                MyWorker(
                    ctx = appContext,
                    parameters = workerParameters
                )
            }
            else -> {
                null
            }
        }
    }
}
