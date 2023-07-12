package com.justcircleprod.btsquiz.data

import com.justcircleprod.btsquiz.data.models.*
import com.justcircleprod.btsquiz.data.room.AppDatabase
import com.justcircleprod.btsquiz.data.room.constants.AppDatabaseConstants
import com.justcircleprod.btsquiz.data.room.constants.DifficultyState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val db: AppDatabase
) {
    suspend fun getRandomTextQuestions(
        countOfQuestions: Int,
        lowerPoints: Int,
        upperPoints: Int,
        noQuestionRepetition: Boolean
    ): Array<TextQuestion> {
        var ids = db.textQuestionDao().getIds(lowerPoints, upperPoints)

        if (noQuestionRepetition) {
            val passedQuestionsId =
                getPassedQuestionsId(QuestionContentType.TEXT_CONTENT_TYPE)
            ids = ids.filter { it !in passedQuestionsId }
        }

        ids = ids.shuffled().take(countOfQuestions)

        return db.textQuestionDao().getByIds(ids.toIntArray())
    }

    suspend fun getRandomImageQuestions(
        countOfQuestions: Int,
        lowerPoints: Int,
        upperPoints: Int,
        noQuestionRepetition: Boolean
    ): Array<ImageQuestion> {
        var ids = db.imageQuestionDao().getIds(lowerPoints, upperPoints)

        if (noQuestionRepetition) {
            val passedQuestionsId =
                getPassedQuestionsId(QuestionContentType.IMAGE_CONTENT_TYPE)
            ids = ids.filter { it !in passedQuestionsId }
        }

        ids = ids.shuffled().take(countOfQuestions)

        return db.imageQuestionDao().getByIds(ids.toIntArray())
    }

    suspend fun getRandomAudioQuestions(
        countOfQuestions: Int,
        lowerPoints: Int,
        upperPoints: Int,
        noQuestionRepetition: Boolean
    ): Array<AudioQuestion> {
        var ids = db.audioQuestionDao().getIds(lowerPoints, upperPoints)

        if (noQuestionRepetition) {
            val passedQuestionsId =
                getPassedQuestionsId(QuestionContentType.AUDIO_CONTENT_TYPE)
            ids = ids.filter { it !in passedQuestionsId }
        }

        ids = ids.shuffled().take(countOfQuestions)

        return db.audioQuestionDao().getByIds(ids.toIntArray())
    }

    suspend fun getRandomVideoQuestions(
        countOfQuestions: Int,
        lowerPoints: Int,
        upperPoints: Int,
        noQuestionRepetition: Boolean
    ): Array<VideoQuestion> {
        var ids = db.videoQuestionDao().getIds(lowerPoints, upperPoints)

        if (noQuestionRepetition) {
            val passedQuestionsId =
                getPassedQuestionsId(QuestionContentType.VIDEO_CONTENT_TYPE)
            ids = ids.filter { it !in passedQuestionsId }
        }

        ids = ids.shuffled().take(countOfQuestions)

        return db.videoQuestionDao().getByIds(ids.toIntArray())
    }

    suspend fun getRandomQuestions(
        countOfQuestions: Int,
        lowerPoints: Int,
        upperPoints: Int,
        noQuestionRepetition: Boolean
    ): List<Question> {
        // questionsIds[?][0] - id,
        // questionsIds[?][1] - QuestionContentType ordinal,
        var questionsIds: MutableList<List<Int>> = mutableListOf()


        var textQuestionsIds = getTextQuestionIds(lowerPoints, upperPoints)

        if (noQuestionRepetition) {
            val passedQuestionsId =
                getPassedQuestionsId(QuestionContentType.TEXT_CONTENT_TYPE)
            textQuestionsIds = textQuestionsIds.filter { it !in passedQuestionsId }
        }

        questionsIds.addAll(textQuestionsIds.map {
            listOf(it, QuestionContentType.TEXT_CONTENT_TYPE.ordinal)
        })


        var imageQuestionsIds = getImageQuestionIds(lowerPoints, upperPoints)

        if (noQuestionRepetition) {
            val passedQuestionsId =
                getPassedQuestionsId(QuestionContentType.IMAGE_CONTENT_TYPE)
            imageQuestionsIds = imageQuestionsIds.filter { it !in passedQuestionsId }
        }

        questionsIds.addAll(imageQuestionsIds.map {
            listOf(it, QuestionContentType.IMAGE_CONTENT_TYPE.ordinal)
        })


        var audioQuestionsIds = getAudioQuestionIds(lowerPoints, upperPoints)

        if (noQuestionRepetition) {
            val passedQuestionsId =
                getPassedQuestionsId(QuestionContentType.AUDIO_CONTENT_TYPE)
            audioQuestionsIds = audioQuestionsIds.filter { it !in passedQuestionsId }
        }

        questionsIds.addAll(audioQuestionsIds.map {
            listOf(it, QuestionContentType.AUDIO_CONTENT_TYPE.ordinal)
        })


        // add Video Questions if difficultyState != USUAL
        val difficultyState =
            DifficultyState.fromInt(getSetting(AppDatabaseConstants.DIFFICULTY_SETTING_ID).state)

        if (difficultyState != DifficultyState.USUAL) {
            var videoQuestionsIds = getVideoQuestionIds(lowerPoints, upperPoints)

            if (noQuestionRepetition) {
                val passedQuestionsId =
                    getPassedQuestionsId(QuestionContentType.VIDEO_CONTENT_TYPE)
                videoQuestionsIds = videoQuestionsIds.filter { it !in passedQuestionsId }
            }

            questionsIds.addAll(videoQuestionsIds.map {
                listOf(it, QuestionContentType.VIDEO_CONTENT_TYPE.ordinal)
            })
        }

        questionsIds = questionsIds.shuffled().take(countOfQuestions).toMutableList()


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

    private suspend fun getTextQuestionIds(lowerPoints: Int, upperPoints: Int) =
        db.textQuestionDao().getIds(lowerPoints, upperPoints)

    private suspend fun getImageQuestionIds(lowerPoints: Int, upperPoints: Int) =
        db.imageQuestionDao().getIds(lowerPoints, upperPoints)

    private suspend fun getAudioQuestionIds(lowerPoints: Int, upperPoints: Int) =
        db.audioQuestionDao().getIds(lowerPoints, upperPoints)

    private suspend fun getVideoQuestionIds(lowerPoints: Int, upperPoints: Int) =
        db.videoQuestionDao().getIds(lowerPoints, upperPoints)

    private suspend fun getPassedQuestionsId(questionContentType: QuestionContentType) =
        db.passedQuestionDao().getIdsByContentType(questionContentType)

    suspend fun insertPassedQuestion(passedQuestion: PassedQuestion) {
        db.passedQuestionDao().insert(passedQuestion)
    }

    suspend fun deleteAllPassedQuestions() =
        db.passedQuestionDao().deleteAll()

    suspend fun getSetting(id: Int) = db.settingDao().getById(id)

    suspend fun updateSetting(setting: Setting) {
        db.settingDao().update(setting)
    }

    fun getScores() = db.scoreDao().getAll()

    suspend fun getScore(id: Int) = db.scoreDao().getById(id)

    suspend fun updateScore(id: Int, score: Int) {
        db.scoreDao().update(id, score)
    }
}