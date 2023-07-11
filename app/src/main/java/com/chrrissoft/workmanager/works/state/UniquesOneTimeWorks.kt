package com.chrrissoft.workmanager.works.state

import androidx.work.ExistingWorkPolicy
import androidx.work.ExistingWorkPolicy.KEEP
import com.chrrissoft.workmanager.request.state.OneTimeWorkRequestOwn
import java.util.*

data class UniquesOneTimeWorks(
    val name: String,
    val id: UUID = UUID.randomUUID(),
    val policy: ExistingWorkPolicy = KEEP,
    val requests: Map<UUID, List<OneTimeWorkRequestOwn>> = mapOf(),
) {
    constructor(count: Int) : this(name = "Uniques one time $count")

    val empty = requests.isEmpty()
}
