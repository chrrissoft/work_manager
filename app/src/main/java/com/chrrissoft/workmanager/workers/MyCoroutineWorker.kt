package com.chrrissoft.workmanager.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class MyCoroutineWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted private val parameters: WorkerParameters
) : CoroutineWorker(ctx, parameters) {
    override suspend fun doWork(): Result {

        repeat(10) {
            delay(2000)
            val progress = Data.Builder().putInt(
                COMMON_PROGRESS_DATA, it
            ).build()
            setProgress(progress)
        }

        val output = Data.Builder()
            .putString(
                /* key = */ COMMON_OUTPUT_DATA,
                /* value = */ "success work from coroutine worker"
            )
        return Result.success(output.build())
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(ctx)
    }

    companion object {
        val NAME = MyCoroutineWorker::class.qualifiedName!!
    }
}
