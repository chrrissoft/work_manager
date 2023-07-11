package com.chrrissoft.workmanager

import androidx.work.Configuration
import javax.inject.Inject

class CustomConfiguration @Inject constructor(
    workerFactory: MyWorkerFactory
) {
    val configuration = Configuration.Builder()
    .setWorkerFactory(workerFactory)
    .build()
}
