package com.justcircleprod.btsquiz.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.core.data.models.questions.TextQuestion

@Dao
interface TextQuestionDao {

    @Query("SELECT * FROM text_questions WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): List<TextQuestion>

    @Query("SELECT COUNT(id) FROM text_questions")
    suspend fun getCount(): Int

    @Query("SELECT id FROM text_questions WHERE points = :points")
    suspend fun getIds(points: Int): List<Int>
}