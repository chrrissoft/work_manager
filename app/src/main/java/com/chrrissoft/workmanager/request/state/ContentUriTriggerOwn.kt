package com.chrrissoft.workmanager.request.state

import kotlinx.serialization.Serializable

@Serializable
data class ContentUriTriggerOwn(val uri: UriOwn, val isTriggeredForDescendants: Boolean)
