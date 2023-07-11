package com.chrrissoft.workmanager

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WorkManagerApp @Inject constructor() : Application(), Configuration.Provider {
    @Inject
    lateinit var customConfiguration: CustomConfiguration

    override fun getWorkManagerConfiguration(): Configuration {
        return customConfiguration.configuration
    }


    companion object {
        const val WORKS_CHANNEL_ID = "com.chrrissoft.workmanager.workers"
    }
}
