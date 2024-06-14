package com.justcircleprod.btsquiz.core.domain.repositories

import androidx.lifecycle.LiveData
import com.justcircleprod.btsquiz.core.data.models.levels.LockedLevel

interface LockedLevelRepository {

    fun getLockedLevelLiveData(levelId: Int): LiveData<LockedLevel>

    suspend fun unlockLevel(levelId: Int)

    suspend fun lockLevel(levelId: Int)
}