package com.justcircleprod.btsquiz.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.core.data.models.levels.LockedLevel
import kotlinx.coroutines.flow.Flow

@Dao
interface LockedLevelDao {

    @Query("SELECT * FROM locked_levels")
    fun getAll(): Flow<List<LockedLevel>>

    @Query("SELECT id FROM locked_levels WHERE is_opened = 1 ORDER BY id DESC LIMIT 1")
    suspend fun getMostExpensiveUnlockedLevelId(): Int?

    @Query("UPDATE locked_levels SET is_opened = :value WHERE id = :levelId")
    suspend fun updateIsOpenedField(levelId: Int, value: Boolean)
}