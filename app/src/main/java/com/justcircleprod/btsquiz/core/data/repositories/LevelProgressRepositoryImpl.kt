package com.justcircleprod.btsquiz.core.data.repositories

import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.repositories.LevelProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LevelProgressRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : LevelProgressRepository {

    override fun getAll() = db.levelProgressDao().getAll()

    override suspend fun addLevelProgress(levelId: Int, progressToAdd: Int) {
        withContext(Dispatchers.IO) {
            val currentProgress = db.levelProgressDao().getLevelProgress(levelId)
            db.levelProgressDao().updateProgressField(levelId, currentProgress + progressToAdd)
        }
    }

    override suspend fun resetLevelProgress(levelId: Int) {
        withContext(Dispatchers.IO) {
            db.levelProgressDao().updateProgressField(levelId, 0)
        }
    }
}