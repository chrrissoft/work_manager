package com.chrrissoft.workmanager.info

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import com.chrrissoft.workmanager.filterOut
import com.chrrissoft.workmanager.request.COMMON_TAG
import com.chrrissoft.workmanager.request.REQUEST_PREFIX_TAG
import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import com.chrrissoft.workmanager.works.UNIQUE_NAME_TAG_PREFIX
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


fun WorkInfo.getRequestOwnFromTag() : WorkRequestOwn {
    val json = tags.first { it.startsWith(REQUEST_PREFIX_TAG) }
        .substring(REQUEST_PREFIX_TAG.length)
    return Json.decodeFromString(json)
}

fun<T> LiveData<T>.observeTop(owner: LifecycleOwner, observer: Observer<T>) {
    removeObservers(owner)
    observe(owner, observer)
}

fun Set<String>.filterOutTags(): Set<String> {
    return filterOut {
        it.startsWith(COMMON_TAG)
                || it.startsWith(REQUEST_PREFIX_TAG)
                || it.startsWith("com.chrrissoft")
                || it.startsWith(UNIQUE_NAME_TAG_PREFIX)
    }
}
