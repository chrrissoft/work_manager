package com.chrrissoft.workmanager.works.state

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingPeriodicWorkPolicy.KEEP
import com.chrrissoft.workmanager.request.state.PeriodicWorkRequestOwn
import java.util.*

data class UniquesPeriodicWorks(
    val name: String,
    val policy: ExistingPeriodicWorkPolicy = KEEP,
    val request: PeriodicWorkRequestOwn? = null,
    val id: UUID = UUID.randomUUID(),
) {
    constructor(count: Int) : this("Unique Period: $count")

    val isNull = request == null
}
