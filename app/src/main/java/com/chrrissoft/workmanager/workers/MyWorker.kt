package com.chrrissoft.workmanager.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Data.Builder
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MyWorker @AssistedInject constructor(
    @Assisted private val ctx: Context,
    @Assisted private val parameters: WorkerParameters
) : Worker(ctx, parameters) {
    override fun doWork(): Result {

        repeat(10) {
            Thread.sleep(2000)
            val progress = Builder().putInt(
                COMMON_PROGRESS_DATA, it
            ).build()
            setProgressAsync(progress)
        }

        val output = Builder()
            .putString(
                /* key = */ COMMON_OUTPUT_DATA,
                /* value = */ "success work from normal worker"
            )
        return Result.success(output.build())
    }

    override fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(ctx)
    }

    companion object {
        val NAME = MyWorker::class.qualifiedName!!
    }
}
