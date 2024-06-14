package com.justcircleprod.btsquiz.core.data.repositories

import com.justcircleprod.btsquiz.core.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.core.data.models.passedQuestion.QuestionContentType
import com.justcircleprod.btsquiz.core.data.models.questions.Question
import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.constants.QuizConstants
import com.justcircleprod.btsquiz.core.domain.repositories.PassedQuestionRepository
import javax.inject.Inject

class PassedQuestionRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : PassedQuestionRepository {

    override suspend fun getRandomPassedQuestions(): List<Question> {
        val count = db.passedQuestionDao().getCount()
        val ids = (1..count).shuffled().take(QuizConstants.COUNT_OF_QUESTIONS_IN_TEST).toIntArray()

        val passedQuestions = db.passedQuestionDao().getByIds(ids)
        val questions: MutableList<Question> = mutableListOf()

        passedQuestions.forEach {
            when (it.questionContentType) {
                QuestionContentType.TEXT_CONTENT_TYPE -> {
                    questions.addAll(db.textQuestionDao().getByIds(intArrayOf(it.questionId)))
                }

                QuestionContentType.IMAGE_CONTENT_TYPE -> {
                    questions.addAll(db.imageQuestionDao().getByIds(intArrayOf(it.questionId)))
                }

                QuestionContentType.VIDEO_CONTENT_TYPE -> {
                    questions.addAll(db.videoQuestionDao().getByIds(intArrayOf(it.questionId)))
                }

                QuestionContentType.AUDIO_CONTENT_TYPE -> {
                    questions.addAll(db.audioQuestionDao().getByIds(intArrayOf(it.questionId)))
                }
            }
        }

        return questions.toList()
    }

    override suspend fun insertPassedQuestion(passedQuestion: PassedQuestion) {
        db.passedQuestionDao().insert(passedQuestion)
    }

    override suspend fun deleteAllPassedQuestions() =
        db.passedQuestionDao().deleteAll()

    override suspend fun getPassedQuestionsCount() =
        db.passedQuestionDao().getCount()

    override fun getPassedQuestionsCountLiveData() =
        db.passedQuestionDao().getCountLiveData()
}