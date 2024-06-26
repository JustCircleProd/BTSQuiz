package com.justcircleprod.btsquiz.core.domain.repositories

import com.justcircleprod.btsquiz.core.data.models.levels.LockedLevel
import kotlinx.coroutines.flow.Flow

interface LockedLevelRepository {

    fun getLockedLevelFlow(levelId: Int): Flow<LockedLevel>

    suspend fun unlockLevel(levelId: Int)

    suspend fun lockLevel(levelId: Int)
}