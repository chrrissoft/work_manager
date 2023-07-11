package com.chrrissoft.workmanager.info

import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.chrrissoft.workmanager.request.COMMON_TAG
import java.util.*
import javax.inject.Inject

class InfoManager @Inject constructor(
    private val manager: WorkManager,
) {

    fun infoById(id: Set<UUID>): Map<UUID, LiveData<WorkInfo>> {
        val result = mutableMapOf<UUID, LiveData<WorkInfo>>()
        id.forEach {
            result[it] = manager.getWorkInfoByIdLiveData(it)
        }
        return result
    }

    fun infoByTag(tag: Set<String>): Map<String, LiveData<List<WorkInfo>>> {
        val result = mutableMapOf<String, LiveData<List<WorkInfo>>>()
        tag.forEach {
            result[it] = manager.getWorkInfosByTagLiveData(it)
        }
        return result
    }

    fun infoByQuery(queries: Set<WorkQuery>): Map<WorkQuery, LiveData<List<WorkInfo>>> {
        val result = mutableMapOf<WorkQuery, LiveData<List<WorkInfo>>>()
        queries.forEach {
            result[it] = manager.getWorkInfosLiveData(it)
        }
        return result
    }

    fun infoByUniqueName(name: Set<String>): Map<String, LiveData<List<WorkInfo>>> {
        val result = mutableMapOf<String, LiveData<List<WorkInfo>>>()
        name.forEach {
            result[it] = manager.getWorkInfosForUniqueWorkLiveData(it)
        }
        return result
    }

    fun infoAll(): LiveData<List<WorkInfo>> {
        return manager.getWorkInfosByTagLiveData(COMMON_TAG)
    }

}
