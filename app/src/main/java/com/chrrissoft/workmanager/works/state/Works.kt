package com.chrrissoft.workmanager.works.state

import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import java.util.*

data class Works(
    val name: String = "",
    val id: UUID = UUID.randomUUID(),
    val requests: Map<UUID, List<WorkRequestOwn>> = mapOf(),
) {
    constructor(count: Int) : this("Works: $count")

    val empty = requests.isEmpty()
}
