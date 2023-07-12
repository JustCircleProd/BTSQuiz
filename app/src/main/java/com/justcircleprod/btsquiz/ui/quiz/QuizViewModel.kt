package com.justcircleprod.btsquiz.ui.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.justcircleprod.btsquiz.appConstants.QuizCategory
import com.justcircleprod.btsquiz.data.AppRepository
import com.justcircleprod.btsquiz.data.models.PassedQuestion.Companion.toPassedQuestion
import com.justcircleprod.btsquiz.data.models.Question
import com.justcircleprod.btsquiz.data.room.constants.AppDatabaseConstants
import com.justcircleprod.btsquiz.data.room.constants.DifficultyState
import com.justcircleprod.btsquiz.data.room.constants.QuestionsRepetitionState
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: AppRepository,
    state: SavedStateHandle
) : ViewModel() {
    val isLoading = MutableLiveData(true)

    // value to prevent the observer from triggering when setting the default value
    var isFirstStart = true

    val countOfQuestions = 6
    val categoryId = state.get<Int>(QuizActivity.CATEGORY_ID_ARGUMENT_NAME)!!

    // the list of questions (public, because its size is involved in some checks)
    var questions = mutableListOf<Question>()

    private var questionPosition = 0

    // the current question or a sign of the end of questions is placed here
    val question = MutableLiveData<Question?>()

    private var noQuestionRepetition = false

    val score = MutableLiveData(0)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noQuestionRepetition = getNoQuestionsRepetitionValue()
            val (lowerPoints, upperPoints) = getPointBorders()

            when (categoryId) {
                QuizCategory.TEXT_CATEGORY.value -> {
                    setTextQuestions(
                        lowerPoints,
                        upperPoints
                    )
                }
                QuizCategory.IMAGE_CATEGORY.value -> {
                    setImageQuestions(
                        lowerPoints,
                        upperPoints
                    )
                }
                QuizCategory.VIDEO_CATEGORY.value -> {
                    setVideoQuestions(
                        lowerPoints,
                        upperPoints
                    )
                }
                QuizCategory.AUDIO_CATEGORY.value -> {
                    setAudioQuestions(
                        lowerPoints,
                        upperPoints
                    )
                }
                else -> {
                    setRandomQuestions(
                        lowerPoints,
                        upperPoints
                    )
                }
            }
        }
    }

    private suspend fun getNoQuestionsRepetitionValue(): Boolean {
        return withContext(Dispatchers.IO) {
            QuestionsRepetitionState.fromInt(
                repository.getSetting(AppDatabaseConstants.QUESTIONS_REPETITION_SETTING_ID).state
            ) == QuestionsRepetitionState.NO_REPETITION
        }
    }

    private suspend fun getPointBorders(): List<Int> {
        return withContext(Dispatchers.IO) {
            val difficultySetting =
                repository.getSetting(AppDatabaseConstants.DIFFICULTY_SETTING_ID)

            when (DifficultyState.fromInt(difficultySetting.state)) {
                DifficultyState.USUAL -> {
                    listOf(300, 499)
                }
                DifficultyState.DIFFICULT -> {
                    listOf(500, 600)
                }
                else -> {
                    listOf(300, 600)
                }
            }
        }
    }

    private suspend fun setRandomQuestions(
        lowerPoints: Int,
        upperPoints: Int
    ) {
        val randomQuestions = repository.getRandomQuestions(
            countOfQuestions,
            lowerPoints,
            upperPoints,
            noQuestionRepetition = noQuestionRepetition
        )

        questions.addAll(randomQuestions)
        onLoadingEnd()
    }

    private suspend fun setTextQuestions(
        lowerPoints: Int,
        upperPoints: Int
    ) {
        val textQuestions = repository.getRandomTextQuestions(
            countOfQuestions,
            lowerPoints,
            upperPoints,
            noQuestionRepetition
        )

        questions.addAll(textQuestions)
        onLoadingEnd()
    }

    private suspend fun setImageQuestions(
        lowerPoints: Int,
        upperPoints: Int
    ) {
        val imageQuestions = repository.getRandomImageQuestions(
            countOfQuestions,
            lowerPoints,
            upperPoints,
            noQuestionRepetition
        )

        questions.addAll(imageQuestions)
        onLoadingEnd()
    }

    private suspend fun setVideoQuestions(
        lowerPoints: Int,
        upperPoints: Int
    ) {
        val videoQuestions =
            repository.getRandomVideoQuestions(
                countOfQuestions,
                lowerPoints,
                upperPoints,
                noQuestionRepetition
            )

        questions.addAll(videoQuestions)
        onLoadingEnd()
    }

    private suspend fun setAudioQuestions(
        lowerPoints: Int,
        upperPoints: Int
    ) {
        val audioQuestions =
            repository.getRandomAudioQuestions(
                countOfQuestions,
                lowerPoints,
                upperPoints,
                noQuestionRepetition
            )

        questions.addAll(audioQuestions)
        onLoadingEnd()
    }

    // a method that updates the download status
    // and shuffles them when the desired number of questions is reached
    private fun onLoadingEnd() {
        questions = questions.shuffled() as MutableList<Question>
        isLoading.postValue(false)
    }

    fun setQuestionOnCurrentPosition() {
        if (questionPosition < countOfQuestions) {
            question.value = questions[questionPosition]
        } else {
            question.value = null
        }
    }

    fun setQuestionOnNextPosition() {
        if (noQuestionRepetition && question.value != null) {
            addToPassedQuestions()
        }

        questionPosition++
        if (questionPosition < countOfQuestions) {
            question.value = questions[questionPosition]
        } else {
            question.value = null
        }
    }

    private fun addToPassedQuestions() {
        val passedQuestion = question.value!!.toPassedQuestion()

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPassedQuestion(passedQuestion)
        }
    }

    fun checkAnswer(userAnswer: String): Boolean {
        val rightAnswer = listOf(
            question.value!!.firstOption,
            question.value!!.secondOption,
            question.value!!.thirdOption,
            question.value!!.fourthOption,
        )[question.value!!.answerNum - 1]

        return if (userAnswer == rightAnswer) {
            score.value = score.value!! + question.value!!.points
            true
        } else {
            false
        }
    }
}