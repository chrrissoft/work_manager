package com.chrrissoft.workmanager.request.state

import androidx.work.Constraints
import androidx.work.NetworkType
import kotlinx.serialization.Serializable
import java.util.concurrent.TimeUnit

@Serializable
data class ConstraintsOwn(
    val requiredNetworkType: NetworkType = NetworkType.NOT_REQUIRED,
    val requiresCharging: Boolean = false,
    val requiresDeviceIdle: Boolean = false,
    val requiresBatteryNotLow: Boolean = false,
    val requiresStorageNotLow: Boolean = false,
    val contentTriggerUpdateDelayMillis: Long = -1,
    val contentTriggerMaxDelayMillis: Long = -1,
    val contentUriTriggers: Set<ContentUriTriggerOwn> = setOf(),
) {
    fun toOriginal() : Constraints {
        val builder = Constraints.Builder()
            .setRequiredNetworkType(requiredNetworkType)
            .setRequiresCharging(requiresCharging)
            .setRequiresDeviceIdle(requiresDeviceIdle)
            .setRequiresBatteryNotLow(requiresBatteryNotLow)
            .setRequiresStorageNotLow(requiresStorageNotLow)
            .setTriggerContentUpdateDelay(contentTriggerUpdateDelayMillis, TimeUnit.MILLISECONDS)
            .setTriggerContentMaxDelay(contentTriggerMaxDelayMillis, TimeUnit.MILLISECONDS)

        contentUriTriggers.forEach {
            builder.addContentUriTrigger(it.uri.toOriginal(), it.isTriggeredForDescendants)
        }

        return builder.build()
    }
}
