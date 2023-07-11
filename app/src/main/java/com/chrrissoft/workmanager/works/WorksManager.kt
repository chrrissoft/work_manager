package com.chrrissoft.workmanager.works

import androidx.work.WorkManager
import com.chrrissoft.workmanager.works.state.OperationOwn
import com.chrrissoft.workmanager.works.state.UniquesOneTimeWorks
import com.chrrissoft.workmanager.works.state.UniquesPeriodicWorks
import com.chrrissoft.workmanager.works.state.Works
import java.util.*
import javax.inject.Inject

class WorksManager @Inject constructor(
    private val manager: WorkManager,
) {

    fun runWorks(works: Works): OperationOwn {
        val requests = works.requests
            .flatMap { it.value }
            .map { it.copyOwn(id = UUID.randomUUID().toString()).toRequest() }

        val operation = manager.enqueue(requests)
        return OperationOwn(type = OperationOwn.Type.Regular, works.name, operation)
    }

    fun runUniqueOneTime(
        works: UniquesOneTimeWorks,
    ): OperationOwn {
        val requests = works.requests
            .flatMap { it.value }
            .addUniqueNameAsTag(works.name)
            .map { it.copy(id = UUID.randomUUID().toString()).toOriginal() }

        val operation = manager.enqueueUniqueWork(works.name, works.policy, requests)
        return OperationOwn(type = OperationOwn.Type.UniquesOneTime, works.name, operation)
    }

    fun runUniquePeriodic(
        works: UniquesPeriodicWorks,
    ): OperationOwn {
        requireNotNull(works.request)
        val requests = works.request
            .copy(
                id = UUID.randomUUID().toString(),
                tags = works.request.tags.plus(works.name)
            )
            .toPeriodic()
        val operation = manager.enqueueUniquePeriodicWork(works.name, works.policy, requests)
        return OperationOwn(type = OperationOwn.Type.UniquePeriodic, works.name, operation)
    }
}
