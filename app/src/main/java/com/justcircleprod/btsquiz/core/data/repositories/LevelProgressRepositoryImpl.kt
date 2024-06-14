package com.justcircleprod.btsquiz.core.data.repositories

import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.repositories.LevelProgressRepository
import javax.inject.Inject

class LevelProgressRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : LevelProgressRepository {

    override fun getLevelProgressLiveData(levelId: Int) =
        db.levelProgressDao().getLiveData(levelId)

    override suspend fun addLevelProgress(levelId: Int, progressToAdd: Int) {
        val currentProgress = db.levelProgressDao().getLevelProgress(levelId)
        db.levelProgressDao().updateProgressField(levelId, currentProgress + progressToAdd)
    }

    override suspend fun resetLevelProgress(levelId: Int) {
        db.levelProgressDao().updateProgressField(levelId, 0)
    }
}