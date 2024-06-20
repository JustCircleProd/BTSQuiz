package com.justcircleprod.btsquiz.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion

@Dao
interface AudioQuestionDao {

    @Query("SELECT * FROM audio_questions WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): List<AudioQuestion>

    @Query("SELECT COUNT(id) FROM audio_questions")
    suspend fun getCount(): Int

    @Query("SELECT id FROM audio_questions WHERE points = :points")
    suspend fun getIds(points: Int): List<Int>
}