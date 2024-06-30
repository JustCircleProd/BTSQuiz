package com.justcircleprod.btsquiz.core.domain.repositories

import com.justcircleprod.btsquiz.core.data.models.levels.LevelProgress
import kotlinx.coroutines.flow.Flow

interface LevelProgressRepository {

    fun getAll(): Flow<List<LevelProgress>>

    suspend fun addLevelProgress(levelId: Int, progressToAdd: Int)

    suspend fun resetLevelProgress(levelId: Int)
}