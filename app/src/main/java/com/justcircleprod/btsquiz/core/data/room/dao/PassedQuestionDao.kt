package com.justcircleprod.btsquiz.core.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.justcircleprod.btsquiz.core.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.core.data.models.passedQuestion.QuestionContentType

@Dao
interface PassedQuestionDao {

    @Query("SELECT * FROM passed_questions WHERE id IN (:ids)")
    suspend fun getByIds(ids: IntArray): List<PassedQuestion>

    @Query("SELECT question_id FROM passed_questions WHERE question_content_type = :questionContentType")
    suspend fun getIdsByContentType(questionContentType: QuestionContentType): List<Int>

    @Query("SELECT COUNT(id) FROM passed_questions")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(id) FROM passed_questions")
    fun getCountLiveData(): LiveData<Int>

    @Insert
    suspend fun insert(passedQuestions: PassedQuestion)

    @Query("DELETE FROM passed_questions")
    suspend fun deleteAll(): Int
}