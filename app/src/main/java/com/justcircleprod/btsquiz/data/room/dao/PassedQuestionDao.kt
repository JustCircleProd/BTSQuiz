package com.justcircleprod.btsquiz.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.justcircleprod.btsquiz.data.models.PassedQuestion
import com.justcircleprod.btsquiz.data.models.QuestionContentType

@Dao
interface PassedQuestionDao {
    @Query("SELECT question_id FROM passed_questions WHERE question_content_type = :questionContentType")
    suspend fun getIdsByContentType(questionContentType: QuestionContentType): List<Int>

    @Insert
    suspend fun insert(passedQuestions: PassedQuestion)

    @Query("DELETE FROM passed_questions")
    suspend fun deleteAll(): Int
}