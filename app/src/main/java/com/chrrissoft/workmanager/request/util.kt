package com.chrrissoft.workmanager.request

import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkRequest
import com.chrrissoft.workmanager.request.state.WorkRequestOwn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val COMMON_TAG = "COMMON_TAG"
const val REQUEST_PREFIX_TAG = "REQUEST_PREFIX_TAG "

fun<B : WorkRequest.Builder<B, *>> WorkRequest.Builder<B, *>.addTags(
    tags: Set<String>
) : WorkRequest.Builder<B, *> {
    tags.forEach { this.addTag(it) }
    return this
}

fun<B : WorkRequest.Builder<B, *>> WorkRequest.Builder<B, *>.addCommonTags(
    requestOwn: WorkRequestOwn
) : WorkRequest.Builder<B, *> {
    addTag(COMMON_TAG)
    addTag("$REQUEST_PREFIX_TAG${Json.encodeToString(requestOwn)}")
    return this
}

fun<B : WorkRequest.Builder<B, *>> WorkRequest.Builder<B, *>.setExpedited(
    set: Boolean, policy: OutOfQuotaPolicy, own: WorkRequestOwn
) : WorkRequest.Builder<B, *> {
    if (set && own.specOwn.initialDelaySeconds == 0f) { this.setExpedited(policy) }
    return this
}

fun Long.toMinutes() = this.div(1000f).div(60f)
