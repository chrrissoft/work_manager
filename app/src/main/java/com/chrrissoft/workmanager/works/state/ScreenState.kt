package com.chrrissoft.workmanager.works.state


data class ScreenState(
    val selectedWorks: Works? = null,
    val selectedOneTime: UniquesOneTimeWorks? = null,
    val selectedPeriodic: UniquesPeriodicWorks? = null,
    val page: EnqueueWorksScreenPages = EnqueueWorksScreenPages.Works,
)
