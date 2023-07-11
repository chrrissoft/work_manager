package com.chrrissoft.workmanager.request.state

fun WorkRequestOwn.asOneTimeOwn(): OneTimeWorkRequestOwn =
    if (this is OneTimeWorkRequestOwn) this else throw IllegalStateException()

fun WorkRequestOwn.asPeriodicOwn(): PeriodicWorkRequestOwn =
    if (this is PeriodicWorkRequestOwn) this else throw IllegalStateException()
