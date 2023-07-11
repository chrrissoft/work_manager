package com.chrrissoft.workmanager.request.state

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class UriOwn(
    val scheme: String,
    val ssp: String,
    val fragment: String
) {
    fun toOriginal(): Uri = Uri.fromParts(scheme, ssp, fragment)
}
