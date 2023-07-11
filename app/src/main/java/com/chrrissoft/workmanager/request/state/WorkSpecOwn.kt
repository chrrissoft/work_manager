package com.chrrissoft.workmanager.request.state

import androidx.work.BackoffPolicy
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequest.Companion.MIN_PERIODIC_FLEX_MILLIS
import androidx.work.PeriodicWorkRequest.Companion.MIN_PERIODIC_INTERVAL_MILLIS
import androidx.work.WorkRequest.Companion.DEFAULT_BACKOFF_DELAY_MILLIS
import com.chrrissoft.workmanager.request.toMinutes
import kotlinx.serialization.Serializable

@Serializable
data class WorkSpecOwn(
    val initialDelaySeconds: Float = 0f,
    val inputData: Map<String, String> = mapOf(),
    val constraints: ConstraintsOwn = ConstraintsOwn(),
    val backoffPolicy: BackoffPolicy = BackoffPolicy.EXPONENTIAL,
    val flexMinutes: Float = MIN_PERIODIC_FLEX_MILLIS.toMinutes(),
    val intervalMinutes: Float = MIN_PERIODIC_INTERVAL_MILLIS.toMinutes(),
    val backoffDelayMinutes: Float = DEFAULT_BACKOFF_DELAY_MILLIS.toMinutes(),
    val outOfQuotaPolicy: OutOfQuotaPolicy = OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST,
)
