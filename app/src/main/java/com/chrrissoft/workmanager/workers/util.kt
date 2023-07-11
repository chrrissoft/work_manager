package com.chrrissoft.workmanager.workers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import com.chrrissoft.workmanager.WorkManagerApp
import kotlin.random.Random

const val COMMON_OUTPUT_DATA = "COMMON_OUTPUT_DATA"
const val COMMON_PROGRESS_DATA = "COMMON_PROGRESS_DATA"

fun getForegroundInfo(ctx: Context): ForegroundInfo {
    val notification = NotificationCompat
        .Builder(ctx, WorkManagerApp.WORKS_CHANNEL_ID)
        .setSubText("Expedited work")
        .setSmallIcon(android.R.drawable.sym_def_app_icon)
        .build()

    return ForegroundInfo(Random.nextInt(), notification)
}