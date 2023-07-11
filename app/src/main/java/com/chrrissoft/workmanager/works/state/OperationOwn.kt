package com.chrrissoft.workmanager.works.state

import androidx.work.Operation


data class OperationOwn(
    val type: Type,
    val name: String,
    val operation: Operation,
)  {
    enum class Type {
        Regular, UniquesOneTime, UniquePeriodic
    }
}
