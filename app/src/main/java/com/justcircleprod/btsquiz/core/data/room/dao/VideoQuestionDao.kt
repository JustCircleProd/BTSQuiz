package com.justcircleprod.btsquiz.core.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion

@Dao
interface VideoQuestionDao {

    @Query("SELECT * FROM video_questions WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): Array<VideoQuestion>

    @Query("SELECT COUNT(id) FROM video_questions")
    suspend fun getCount(): Int

    @Query("SELECT id FROM video_questions WHERE points = :points")
    suspend fun getIds(points: Int): List<Int>
}