package com.justcircleprod.btsquiz.core.domain.repositories

import androidx.lifecycle.LiveData
import com.justcircleprod.btsquiz.core.data.models.levels.LevelProgress

interface LevelProgressRepository {

    fun getLevelProgressLiveData(levelId: Int): LiveData<LevelProgress>

    suspend fun addLevelProgress(levelId: Int, progressToAdd: Int)

    suspend fun resetLevelProgress(levelId: Int)
}