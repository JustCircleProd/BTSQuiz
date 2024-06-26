package com.justcircleprod.btsquiz.core.domain.repositories

import com.justcircleprod.btsquiz.core.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.Question
import kotlinx.coroutines.flow.Flow

interface PassedQuestionRepository {

    suspend fun getRandomPassedQuestions(): List<Question>

    suspend fun getPassedQuestionsCount(): Int

    fun getPassedQuestionsCountFlow(): Flow<Int>

    suspend fun insertPassedQuestion(passedQuestion: PassedQuestion)

    suspend fun deleteAllPassedQuestions()
}