package com.justcircleprod.btsquiz.core.data.repositories

import com.justcircleprod.btsquiz.core.data.models.passedQuestion.QuestionContentType
import com.justcircleprod.btsquiz.core.data.models.questions.Question
import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.constants.QuizConstants
import com.justcircleprod.btsquiz.core.domain.repositories.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val db: AppDatabase
) : QuestionRepository {

    override suspend fun getRandomQuestions(points: Int): List<Question> {
        // questionsIds[?][0] - id,
        // questionsIds[?][1] - QuestionContentType ordinal,
        var questionsIds: MutableList<List<Int>> = mutableListOf()


        var textQuestionsIds = db.textQuestionDao().getIds(points)

        textQuestionsIds =
            textQuestionsIds.filter {
                it !in db.passedQuestionDao()
                    .getIdsByContentType(QuestionContentType.TEXT_CONTENT_TYPE)
            }

        questionsIds.addAll(textQuestionsIds.map {
            listOf(it, QuestionContentType.TEXT_CONTENT_TYPE.ordinal)
        })


        var imageQuestionsIds = db.imageQuestionDao().getIds(points)

        imageQuestionsIds =
            imageQuestionsIds.filter {
                it !in db.passedQuestionDao()
                    .getIdsByContentType(QuestionContentType.IMAGE_CONTENT_TYPE)
            }

        questionsIds.addAll(imageQuestionsIds.map {
            listOf(it, QuestionContentType.IMAGE_CONTENT_TYPE.ordinal)
        })


        var audioQuestionsIds = db.audioQuestionDao().getIds(points)

        audioQuestionsIds =
            audioQuestionsIds.filter {
                it !in db.passedQuestionDao()
                    .getIdsByContentType(QuestionContentType.AUDIO_CONTENT_TYPE)
            }

        questionsIds.addAll(audioQuestionsIds.map {
            listOf(it, QuestionContentType.AUDIO_CONTENT_TYPE.ordinal)
        })


        // add Video Questions if points >= 500
        if (points >= 500) {
            var videoQuestionsIds = db.videoQuestionDao().getIds(points)

            videoQuestionsIds =
                videoQuestionsIds.filter {
                    it !in db.passedQuestionDao()
                        .getIdsByContentType(QuestionContentType.VIDEO_CONTENT_TYPE)
                }

            questionsIds.addAll(videoQuestionsIds.map {
                listOf(it, QuestionContentType.VIDEO_CONTENT_TYPE.ordinal)
            })
        }

        questionsIds =
            questionsIds.shuffled().take(QuizConstants.COUNT_OF_QUESTIONS_IN_TEST).toMutableList()


        val questions: MutableList<Question> = mutableListOf()

        questionsIds.forEach {
            when (it[1]) {
                QuestionContentType.TEXT_CONTENT_TYPE.ordinal -> {
                    questions.addAll(
                        db.textQuestionDao().getByIds(intArrayOf(it[0]))
                    )
                }

                QuestionContentType.IMAGE_CONTENT_TYPE.ordinal -> {
                    questions.addAll(
                        db.imageQuestionDao().getByIds(intArrayOf(it[0]))
                    )
                }

                QuestionContentType.AUDIO_CONTENT_TYPE.ordinal -> {
                    questions.addAll(
                        db.audioQuestionDao().getByIds(intArrayOf(it[0]))
                    )
                }

                QuestionContentType.VIDEO_CONTENT_TYPE.ordinal -> {
                    questions.addAll(
                        db.videoQuestionDao().getByIds(intArrayOf(it[0]))
                    )
                }
            }
        }

        return questions
    }
}