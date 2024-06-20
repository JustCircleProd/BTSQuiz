package com.justcircleprod.btsquiz.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.core.data.models.questions.ImageQuestion

@Dao
interface ImageQuestionDao {

    @Query("SELECT * FROM image_questions WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): List<ImageQuestion>

    @Query("SELECT COUNT(id) FROM image_questions")
    suspend fun getCount(): Int

    @Query("SELECT id FROM image_questions WHERE points = :points")
    suspend fun getIds(points: Int): List<Int>
}