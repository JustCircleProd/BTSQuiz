package com.justcircleprod.btsquiz.core.domain.repositories

import com.justcircleprod.btsquiz.core.data.models.questions.Question

interface QuestionRepository {

    suspend fun getRandomQuestions(points: Int): List<Question>
}