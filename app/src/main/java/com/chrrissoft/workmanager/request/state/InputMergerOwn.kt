package com.chrrissoft.workmanager.request.state

import androidx.work.ArrayCreatingInputMerger
import androidx.work.InputMerger
import androidx.work.OverwritingInputMerger

enum class InputMergerOwn {
    OverwritingInputMergerOwn, ArrayCreatingInputMergerOwn;

    val mergerName get() = when(this) {
        OverwritingInputMergerOwn -> "Overwriting"
        ArrayCreatingInputMergerOwn -> "Array Creating"
    }

    fun toOriginal() : Class<out InputMerger> {
        return when(this) {
            OverwritingInputMergerOwn -> OverwritingInputMerger::class.java
            ArrayCreatingInputMergerOwn -> ArrayCreatingInputMerger::class.java
        }
    }
}