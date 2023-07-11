package com.chrrissoft.workmanager.request.state

import androidx.work.WorkRequest
import com.chrrissoft.workmanager.workers.WorkerType
import kotlinx.serialization.Serializable

@Serializable
sealed interface WorkRequestOwn {
    val id: String
    val name: String
    val tags: Set<String>
    val worker: WorkerType
    val expedited: Boolean
    val specOwn: WorkSpecOwn

    val merger: InputMergerOwn?
    val typeName : String

    fun copyOwn(
        id: String = this.id,
        name: String = this.name,
        tags: Set<String> = this.tags,
        worker: WorkerType = this.worker,
        expedited: Boolean = this.expedited,
        specOwn: WorkSpecOwn = this.specOwn,
    ): WorkRequestOwn

    fun toRequest() : WorkRequest
}
