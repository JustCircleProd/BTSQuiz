package com.justcircleprod.btsquiz.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.data.models.Score

@Dao
interface ScoreDao {
    @Query("SELECT * FROM scores")
    fun getAll(): List<Score>
}