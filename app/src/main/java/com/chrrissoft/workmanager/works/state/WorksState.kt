package com.chrrissoft.workmanager.works.state

data class WorksState(
    val works: List<Works> = emptyList(),
    val operations: List<OperationOwn> = emptyList(),
    val uniquesOneTimeWorks: List<UniquesOneTimeWorks> = emptyList(),
    val uniquesPeriodicWorks: List<UniquesPeriodicWorks> = emptyList(),
)
