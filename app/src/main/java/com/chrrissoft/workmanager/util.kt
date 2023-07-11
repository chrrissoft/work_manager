package com.chrrissoft.workmanager

import java.util.*

fun<T> List<T>.filterOut(predicate: (T) -> Boolean): List<T> {
    return filter { !predicate(it) }
}

fun<T> Set<T>.filterOut(predicate: (T) -> Boolean): Set<T> {
    return filter { !predicate(it) }.toSet()
}


fun String.toUUID(): UUID {
    return UUID.fromString(this)
}
