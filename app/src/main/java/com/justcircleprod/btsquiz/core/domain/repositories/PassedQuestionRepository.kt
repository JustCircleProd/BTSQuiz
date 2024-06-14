package com.justcircleprod.btsquiz.core.domain.repositories

import androidx.lifecycle.LiveData
import com.justcircleprod.btsquiz.core.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.Question

interface PassedQuestionRepository {

    suspend fun getRandomPassedQuestions(): List<Question>

    suspend fun insertPassedQuestion(passedQuestion: PassedQuestion)

    suspend fun deleteAllPassedQuestions(): Int

    suspend fun getPassedQuestionsCount(): Int

    fun getPassedQuestionsCountLiveData(): LiveData<Int>
}