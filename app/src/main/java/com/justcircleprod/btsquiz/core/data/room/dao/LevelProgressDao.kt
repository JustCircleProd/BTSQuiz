package com.justcircleprod.btsquiz.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.core.data.models.levels.LevelProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelProgressDao {

    @Query("SELECT * FROM levels_progress")
    fun getAll(): Flow<List<LevelProgress>>

    @Query("SELECT progress FROM levels_progress WHERE id = :levelId")
    suspend fun getLevelProgress(levelId: Int): Int

    @Query("UPDATE levels_progress SET progress = :progress WHERE id = :levelId")
    suspend fun updateProgressField(levelId: Int, progress: Int)
}