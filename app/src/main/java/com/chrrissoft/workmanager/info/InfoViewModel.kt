package com.chrrissoft.workmanager.info

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkQuery
import com.chrrissoft.workmanager.info.InfoEvent.*
import com.chrrissoft.workmanager.info.InfoState.ObservablesWorks
import com.chrrissoft.workmanager.info.InfoState.WorkInfoOwn
import com.chrrissoft.workmanager.works.UNIQUE_NAME_TAG_PREFIX
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val manager: InfoManager,
) : ViewModel() {

    private val handler = EventHandler()

    private lateinit var owner: LifecycleOwner

    private val _state = MutableStateFlow(InfoState())
    val state = _state.asStateFlow()

    private val idsState = MutableStateFlow(mapOf<UUID, WorkInfoOwn>())
    private val tagsState = MutableStateFlow(mapOf<String, List<WorkInfoOwn>>())
    private val uniqueNamesState = MutableStateFlow(mapOf<String, List<WorkInfoOwn>>())
    private val queriesState = MutableStateFlow(mapOf<WorkQuery, List<WorkInfoOwn>>())

    init {
        viewModelScope.launch {
            idsState.collect { infos ->
                _state.update { state ->
                    state.copy(infosById = InfoState.InfoByIds(infos))
                }
            }
        }
        viewModelScope.launch {
            tagsState.collect { infos ->
                _state.update { state ->
                    state.copy(infosByTag = InfoState.InfoByTags(infos))
                }
            }
        }
        viewModelScope.launch {
            uniqueNamesState.collect { infos ->
                _state.update { state ->
                    state.copy(infoByUniqueNames = InfoState.InfoByUniqueNames(infos))
                }
            }
        }
        viewModelScope.launch {
            queriesState.collect { infos ->
                _state.update { state ->
                    state.copy(infosByQuery = InfoState.InfoByQuery(infos))
                }
            }
        }
    }

    fun handleEvent(event: InfoEvent) {
        event.handle(handler)
    }


    inner class EventHandler {
        fun onEvent(event: OnInfoById) {
            if (!::owner.isInitialized) return
            observeInfoByIds(owner, manager.infoById(event.ids))
        }

        fun onEvent(event: OnInfoByTag) {
            if (!::owner.isInitialized) return
            observeInfoByTags(owner, manager.infoByTag(event.tags))
        }

        fun onEvent(event: OnInfoByUniqueName) {
            if (!::owner.isInitialized) return
            observeInfoByUniqueNames(owner, manager.infoByUniqueName(event.names))
        }

        fun onEvent(event: OnInfoByQuery) {
            if (!::owner.isInitialized) return
            Log.d(TAG, "requests data: ${event.query.size}")
            observeInfoByQueries(owner, manager.infoByQuery(event.query))
        }

        fun onEvent(event: OnChangePage) {
            _state.update {
                it.copy(page = event.page)
            }
        }

        fun onEvent(event: OnInitializeObservableWorks) {
            if (::owner.isInitialized) return
            owner = event.owner
            observeObservableWorks(owner)
        }
    }

    private fun observeObservableWorks(owner: LifecycleOwner) {
        manager.infoAll().observe(owner) { managerInfo ->
            _state.update { state ->
                val observablesWorks = ObservablesWorks(
                    ids = managerInfo.map { it.id }.toSet(),
                    tags = managerInfo
                        .map { it.tags }
                        .flatten()
                        .toSet()
                        .filterOutTags(),
                    uniqueName = managerInfo
                        .mapNotNull { info ->
                            info.tags
                                .firstOrNull { it.startsWith(UNIQUE_NAME_TAG_PREFIX) }
                                ?.substring(UNIQUE_NAME_TAG_PREFIX.length)
                        }
                        .toSet()
                )
                state.copy(observablesWorks = observablesWorks)
            }
        }
    }

    private fun observeInfoByIds(
        owner: LifecycleOwner,
        data: Map<UUID, LiveData<WorkInfo>>,
    ) {
        idsState.update { emptyMap() }
        data.forEach { lifeInfo ->
            lifeInfo.value.observeTop(owner) { info ->
                idsState.update {
                    val mutableInfo = it.toMutableMap()
                    mutableInfo[lifeInfo.key] = WorkInfoOwn(info, info.getRequestOwnFromTag())
                    mutableInfo
                }
            }
        }
    }

    private fun observeInfoByTags(
        owner: LifecycleOwner,
        data: Map<String, LiveData<List<WorkInfo>>>,
    ) {
        tagsState.update { emptyMap() }
        data.forEach { lifeInfo ->
            lifeInfo.value.observeTop(owner) { infos ->
                tagsState.update { state ->
                    val mutableInfo = state.toMutableMap()
                    mutableInfo[lifeInfo.key] = infos.map {
                        WorkInfoOwn(it, it.getRequestOwnFromTag())
                    }
                    mutableInfo
                }
            }
        }
    }

    private fun observeInfoByUniqueNames(
        owner: LifecycleOwner,
        data: Map<String, LiveData<List<WorkInfo>>>,
    ) {
        uniqueNamesState.update { emptyMap() }
        data.forEach { lifeInfo ->
            lifeInfo.value.observeTop(owner) { infos ->
                uniqueNamesState.update { state ->
                    val mutableInfo = state.toMutableMap()
                    mutableInfo[lifeInfo.key] = infos.map {
                        Log.d(TAG, "mapping")
                        WorkInfoOwn(it, it.getRequestOwnFromTag())
                    }
                    mutableInfo
                }
            }
        }
    }

    private fun observeInfoByQueries(
        owner: LifecycleOwner,
        data: Map<WorkQuery, LiveData<List<WorkInfo>>>,
    ) {
        Log.d(TAG, "received data: ${data.size}")
        queriesState.update { emptyMap() }
        data.forEach { lifeInfo ->
            lifeInfo.value.observeTop(owner) { infos ->
                queriesState.update { state ->
                    val mutableInfo = state.toMutableMap()
                    mutableInfo[lifeInfo.key] = infos.map {
                        WorkInfoOwn(it, it.getRequestOwnFromTag())
                    }
                    mutableInfo
                }
            }
        }
    }

    companion object {
        private const val TAG = "InfoViewModel"
    }
}
