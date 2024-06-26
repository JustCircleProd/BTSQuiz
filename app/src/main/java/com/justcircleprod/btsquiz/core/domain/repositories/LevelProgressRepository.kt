package com.justcircleprod.btsquiz.core.domain.repositories

import com.justcircleprod.btsquiz.core.data.models.levels.LevelProgress
import kotlinx.coroutines.flow.Flow

interface LevelProgressRepository {

    fun getLevelProgressFlow(levelId: Int): Flow<LevelProgress>

    suspend fun addLevelProgress(levelId: Int, progressToAdd: Int)

    suspend fun resetLevelProgress(levelId: Int)
}