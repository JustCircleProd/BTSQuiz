package com.justcircleprod.btsquiz.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.data.models.levels.LevelProgress

@Dao
interface LevelProgressDao {
    @Query("SELECT * FROM levels_progress WHERE id = :levelId")
    fun getLiveData(levelId: Int): LiveData<LevelProgress>

    @Query("SELECT progress FROM levels_progress WHERE id = :levelId")
    suspend fun getLevelProgress(levelId: Int): Int

    @Query("UPDATE levels_progress SET progress = :progress WHERE id = :levelId")
    suspend fun updateProgressField(levelId: Int, progress: Int)
}