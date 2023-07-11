@file:SuppressLint("ModifierParameter")

package com.chrrissoft.workmanager.request.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.work.BackoffPolicy.EXPONENTIAL
import androidx.work.BackoffPolicy.LINEAR
import androidx.work.OutOfQuotaPolicy.DROP_WORK_REQUEST
import androidx.work.OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST
import com.chrrissoft.workmanager.request.RequestEvent
import com.chrrissoft.workmanager.request.RequestEvent.*
import com.chrrissoft.workmanager.request.state.InputMergerOwn.ArrayCreatingInputMergerOwn
import com.chrrissoft.workmanager.request.state.InputMergerOwn.OverwritingInputMergerOwn
import com.chrrissoft.workmanager.request.state.OneTimeWorkRequestOwn
import com.chrrissoft.workmanager.request.state.PeriodicWorkRequestOwn
import com.chrrissoft.workmanager.request.state.RequestState
import com.chrrissoft.workmanager.request.state.RequestType
import com.chrrissoft.workmanager.ui.components.MyTextField

@Composable
fun RequestBuilder(
    state: RequestState,
    modifier: Modifier = Modifier,
    onEvent: (RequestEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .clip(shapes.extraLarge)
            .background(colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        MyTextField(
            value = state.request.name,
            onValueChange = {
                onEvent(OnChangeRequestName(it))
            },
            placeholder = {
                Text(text = "Request title")
            },
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        )

        RequestBuilderSettingRowContainer {
            DataUi(
                data = state.request.specOwn.inputData,
                onAddData = {
                    onEvent(OnAddData(it))
                },
                onDeleteTag = {
                    onEvent(OnDeleteData(it))
                }
            )
        }

        RequestBuilderSettingRowContainer {
            TagsUi(
                tag = state.request.tags,
                onAddTag = {
                    onEvent(OnAddTag(it))
                },
                onDeleteTag = {
                    onEvent(OnDeleteTag(it))
                }
            )
        }

        RequestBuilderSettingRowContainer {
            ConstraintsUi(
                constraints = state.request.specOwn.constraints,
                onChangeConstraints = {
                    onEvent(OnConstraints(it))
                }
            )
        }

        RequestBuilderSettingRowContainer {
            Changers(
                expedited = state.request.expedited,
                expeditedEnabled = state.request.specOwn.initialDelaySeconds == 0f,
                flexInterval = (state.request is PeriodicWorkRequestOwn && state.request.flexInterval),
                requestType = RequestType.getFromRequestOnw(state.request),
                workerType = state.request.worker,
                onChangeFlexInterval = {
                    onEvent(OnEnableFlexInterval(it))
                },
                onChangeExpedited = {
                    onEvent(OnEnableExpedited(it))
                },
                onChangeRequestType = {
                    onEvent(OnRequestType(it))
                },
                onChangeWorkerType = {
                    onEvent(OnWorker(it))
                },
                flexIntervalAllowed = state.request is PeriodicWorkRequestOwn
            )
        }

        RequestBuilderSettingRowContainer {
            IntervalsIU(
                flexMinutes = state.request.specOwn.flexMinutes,
                intervalMinutes = state.request.specOwn.intervalMinutes,
                modifier = Modifier.weight(1f),
                onChangeFlexMinutes = { onEvent(OnFlexInterval(it)) },
                onChangeIntervalMinutes = { onEvent(OnPeriodicInterval(it)) },
                flexEnabled = state.request is PeriodicWorkRequestOwn && state.request.flexInterval,
                intervalEnabled = state.request is PeriodicWorkRequestOwn
            )
        }

        RequestBuilderSettingColumContainer {
            OutOfQuotaPolicy(
                enabled = state.request.expedited,
                policy = state.request.specOwn.outOfQuotaPolicy,
                policies = listOf(DROP_WORK_REQUEST, RUN_AS_NON_EXPEDITED_WORK_REQUEST),
                onChangePolicy = {
                    onEvent(OnOutOfQuotaPolicy(it))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            InputMerger(
                merger = state.request.merger,
                allowedMergers = listOf(OverwritingInputMergerOwn, ArrayCreatingInputMergerOwn),
                enable = state.request is OneTimeWorkRequestOwn,
                onChangeMerger = {
                    onEvent(OnInputMerger(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        RequestBuilderSettingColumContainer {
            InitialDelayUI(
                delay = state.request.specOwn.initialDelaySeconds,
                onChangeDelay = {
                    onEvent(OnInitialDelay(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            BackoffPolicy(
                policy = state.request.specOwn.backoffPolicy,
                policies = listOf(LINEAR, EXPONENTIAL),
                onChangeBackoffCriteria = { policy, minutes ->
                    onEvent(OnBackoffCriteria(policy, minutes))
                },
                backoffMinutes = state.request.specOwn.backoffDelayMinutes,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@SuppressLint("ComposableModifierFactory", "ModifierFactoryExtensionFunction")
@Composable
private fun commonModifier(modifier: Modifier = Modifier): Modifier = modifier
    .padding(horizontal = 10.dp, vertical = 7.dp)
    .fillMaxWidth()
    .clip(shapes.extraLarge)
    .background(colorScheme.onPrimary)
    .padding(10.dp)

@Composable
private fun RequestBuilderSettingRowContainer(
    modifier: Modifier = commonModifier(),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) { content() }
}

@Composable
private fun RequestBuilderSettingColumContainer(
    modifier: Modifier = commonModifier(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) { content() }
}

