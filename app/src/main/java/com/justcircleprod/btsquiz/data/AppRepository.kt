package com.justcircleprod.btsquiz.data

import androidx.datastore.preferences.core.stringPreferencesKey
import com.justcircleprod.btsquiz.common.AppConstants
import com.justcircleprod.btsquiz.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.data.dataStore.DataStoreManager
import com.justcircleprod.btsquiz.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.data.models.passedQuestion.QuestionContentType
import com.justcircleprod.btsquiz.data.models.questions.Question
import com.justcircleprod.btsquiz.data.room.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val db: AppDatabase,
    private val dataStoreManager: DataStoreManager
) {
    suspend fun getRandomQuestions(points: Int): List<Question> {
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
            questionsIds.shuffled().take(AppConstants.COUNT_OF_QUESTIONS_IN_TEST).toMutableList()


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

    suspend fun getRandomPassedQuestions(): List<Question> {
        val count = db.passedQuestionDao().getCount()
        val ids = (1..count).shuffled().take(AppConstants.COUNT_OF_QUESTIONS_IN_TEST).toIntArray()

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

    suspend fun insertPassedQuestion(passedQuestion: PassedQuestion) {
        db.passedQuestionDao().insert(passedQuestion)
    }

    suspend fun deleteAllPassedQuestions() =
        db.passedQuestionDao().deleteAll()

    suspend fun getPassedQuestionsCount() =
        db.passedQuestionDao().getCount()

    fun getPassedQuestionsCountLiveData() =
        db.passedQuestionDao().getCountLiveData()

    fun getScores() = db.scoreDao().getAll()

    fun getLockedLevelLiveData(levelId: Int) =
        db.lockedLevelDao().getLiveData(levelId)

    suspend fun unlockLevel(levelId: Int) {
        db.lockedLevelDao().updateIsOpenedField(levelId, true)
    }

    suspend fun lockLevel(levelId: Int) {
        db.lockedLevelDao().updateIsOpenedField(levelId, false)
    }

    fun getLevelProgressLiveData(levelId: Int) =
        db.levelProgressDao().getLiveData(levelId)

    suspend fun addLevelProgress(levelId: Int, progressToAdd: Int) {
        val currentProgress = db.levelProgressDao().getLevelProgress(levelId)
        db.levelProgressDao().updateProgressField(levelId, currentProgress + progressToAdd)
    }

    suspend fun resetLevelProgress(levelId: Int) {
        db.levelProgressDao().updateProgressField(levelId, 0)
    }

    fun isIntroductionShown() =
        dataStoreManager.collect(stringPreferencesKey(DataStoreConstants.INTRODUCTION_IS_SHOWN_KEY))

    suspend fun setIntroductionShown() {
        dataStoreManager.edit(
            stringPreferencesKey(DataStoreConstants.INTRODUCTION_IS_SHOWN_KEY),
            DataStoreConstants.INTRODUCTION_IS_SHOWN
        )
    }

    fun getUserCoinsQuantity(): Flow<String?> =
        dataStoreManager.collect(stringPreferencesKey(DataStoreConstants.USER_COINS_QUANTITY_KEY))

    suspend fun editUserCoinsQuantity(value: Int) {
        dataStoreManager.edit(
            stringPreferencesKey(DataStoreConstants.USER_COINS_QUANTITY_KEY),
            value.toString()
        )
    }

    suspend fun addUserCoins(coinsToAdd: Int) {
        val currentCoins = getUserCoinsQuantity().first()?.toInt() ?: return
        editUserCoinsQuantity(currentCoins + coinsToAdd)
    }

    suspend fun subtractUserCoins(coinsToSubtract: Int) {
        val currentCoins = getUserCoinsQuantity().first()?.toInt() ?: return
        editUserCoinsQuantity(currentCoins - coinsToSubtract)
    }

    fun getWithoutQuizHintsState() =
        dataStoreManager.collect(stringPreferencesKey(DataStoreConstants.WITHOUT_QUIZ_HINTS_KEY))

    suspend fun editWithoutQuizHintsState(state: String) {
        dataStoreManager.edit(
            stringPreferencesKey(DataStoreConstants.WITHOUT_QUIZ_HINTS_KEY),
            state
        )
    }
}