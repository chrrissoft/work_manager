package com.chrrissoft.workmanager.request.state

import androidx.work.OneTimeWorkRequest
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
data class OneTimeWorkRequestOwn(
    override val id: String = UUID.randomUUID().toString(),
    override val name: String = "",
    override val worker: WorkerType = WorkerType.Worker,
    override val expedited: Boolean = false,
    override val tags: Set<String> = setOf(),
    override val specOwn: WorkSpecOwn = WorkSpecOwn(),
    val inputMerger: InputMergerOwn = InputMergerOwn.ArrayCreatingInputMergerOwn,
) : WorkRequestOwn {
    override val merger: InputMergerOwn get() = inputMerger
    override val typeName get() = "One Time"

    override fun toRequest(): WorkRequest {
        return toOriginal()
    }

    fun toOriginal() : OneTimeWorkRequest {
        return worker.oneTimeBuilder
            .setInputMerger(inputMerger.toOriginal())
            .setId(UUID.fromString(id))
            .setInitialDelay(specOwn.initialDelaySeconds.toLong(), TimeUnit.SECONDS)
            .setBackoffCriteria(specOwn.backoffPolicy, specOwn.backoffDelayMinutes.toLong(), MINUTES)
            .setConstraints(specOwn.constraints.toOriginal())
            .addTags(tags)
            .setExpedited(expedited, specOwn.outOfQuotaPolicy, this)
            .addCommonTags(this)
            .build() as OneTimeWorkRequest
    }

    override fun copyOwn(
        id: String,
        name: String,
        tags: Set<String>,
        worker: WorkerType,
        expedited: Boolean,
        specOwn: WorkSpecOwn,
    ): WorkRequestOwn {
        return this.copy(
            id = id,
            name = name,
            worker = worker,
            expedited = expedited,
            tags = tags,
            specOwn = specOwn,
            inputMerger = this.inputMerger
        )
    }

}
