package com.justcircleprod.btsquiz.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.data.models.levels.LockedLevel

@Dao
interface LockedLevelDao {
    @Query("SELECT * FROM locked_levels WHERE id = :levelId")
    fun getLiveData(levelId: Int): LiveData<LockedLevel>

    @Query("UPDATE locked_levels SET is_opened = :value WHERE id = :levelId")
    suspend fun updateIsOpenedField(levelId: Int, value: Boolean)
}