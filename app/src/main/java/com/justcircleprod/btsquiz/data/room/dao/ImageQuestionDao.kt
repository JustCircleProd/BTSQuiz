package com.justcircleprod.btsquiz.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.data.models.ImageQuestion

@Dao
interface ImageQuestionDao {
    @Query("SELECT * FROM image_questions WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): Array<ImageQuestion>

    @Query("SELECT COUNT(id) FROM image_questions")
    suspend fun getCount(): Int

    @Query("SELECT id FROM image_questions WHERE points >= :lowerPoints AND points <= :upperPoints")
    suspend fun getIds(lowerPoints: Int, upperPoints: Int): List<Int>
}