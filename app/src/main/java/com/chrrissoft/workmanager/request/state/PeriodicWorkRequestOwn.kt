package com.chrrissoft.workmanager.request.state

import androidx.work.PeriodicWorkRequest
import androidx.work.WorkRequest
import com.chrrissoft.workmanager.request.addCommonTags
import com.chrrissoft.workmanager.request.addTags
import com.chrrissoft.workmanager.request.setExpedited
import com.chrrissoft.workmanager.workers.WorkerType
import kotlinx.serialization.Serializable
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MINUTES

@Serializable
data class PeriodicWorkRequestOwn(
    override val name: String = "",
    override val id: String = UUID.randomUUID().toString(),
    override val worker: WorkerType = WorkerType.Worker,
    override val expedited: Boolean = false,
    override val tags: Set<String> = setOf(),
    override val specOwn: WorkSpecOwn = WorkSpecOwn(),
    val flexInterval: Boolean = false,
) : WorkRequestOwn {
    override val merger: InputMergerOwn? get() = null
    override val typeName: String get() = "Periodic"

    override fun copyOwn(
        id: String,
        name: String,
        tags: Set<String>,
        worker: WorkerType,
        expedited: Boolean,
        specOwn: WorkSpecOwn,
    ): WorkRequestOwn {
        return copy(
            id = id,
            name = name,
            tags = tags,
            worker = worker,
            specOwn = specOwn,
            expedited = expedited,
            flexInterval = this.flexInterval,
        )
    }

    override fun toRequest(): WorkRequest {
        return toPeriodic()
    }

    fun toPeriodic(): PeriodicWorkRequest {
        val builder = if (this.flexInterval) worker.periodicFlexBuilder(
            specOwn.intervalMinutes.toLong(), MINUTES, specOwn.flexMinutes.toLong(), MINUTES)
        else worker.periodicBuilder(specOwn.intervalMinutes.toLong(), MINUTES)

        return builder
            .setExpedited(expedited, specOwn.outOfQuotaPolicy, this)
            .addTags(tags)
            .addCommonTags(this)
            .setId(UUID.fromString(id))
            .setInitialDelay(specOwn.initialDelaySeconds.toLong(), TimeUnit.SECONDS)
            .setBackoffCriteria(specOwn.backoffPolicy,
                specOwn.backoffDelayMinutes.toLong(),
                MINUTES)
            .setConstraints(specOwn.constraints.toOriginal())
            .build()
    }

}
