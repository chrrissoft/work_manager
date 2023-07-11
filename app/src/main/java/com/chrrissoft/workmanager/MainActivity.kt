package com.chrrissoft.workmanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.chrrissoft.workmanager.info.InfoEvent.OnInitializeObservableWorks
import com.chrrissoft.workmanager.info.InfoViewModel
import com.chrrissoft.workmanager.request.WorkRequestViewModel
import com.chrrissoft.workmanager.ui.components.AppUI
import com.chrrissoft.workmanager.ui.navigation.AppNavigation
import com.chrrissoft.workmanager.works.WorksViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestViewModel: WorkRequestViewModel by viewModels()
    private val worksViewModel: WorksViewModel by viewModels()
    private val infoViewModel: InfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppUI {
                AppNavigation(
                    requestState = requestViewModel.state.collectAsState().value,
                    onRequestEvent = {
                        requestViewModel.handleEvent(it)
                    },
                    worksScreenState = worksViewModel.screenState.collectAsState().value,
                    onWorksScreenEvent = {
                        worksViewModel.handleState(it)
                    },
                    enqueuedWorksState = worksViewModel.worksState.collectAsState().value,
                    onWorksEvent = {
                        worksViewModel.handleWorksEvent(it)
                    },
                    infoState = infoViewModel.state.collectAsState().value,
                    onInfoEvent = {
                        infoViewModel.handleEvent(it)
                    }
                )
                setBarsColors()
            }
        }
        infoViewModel.handleEvent(OnInitializeObservableWorks(this))
    }

    companion object {
        @SuppressLint("ComposableNaming")
        @Composable
        fun setBarsColors(
            bottom: Color = colorScheme.primaryContainer,
            status: Color = colorScheme.primaryContainer,
        ) {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()

            DisposableEffect(systemUiController, useDarkIcons) {
                systemUiController.setStatusBarColor(status, useDarkIcons)
                systemUiController.setNavigationBarColor(bottom)
                onDispose {}
            }
        }
    }
}
