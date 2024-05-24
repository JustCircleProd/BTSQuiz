package com.justcircleprod.btsquiz.ui.quiz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.common.CoinConstants
import com.justcircleprod.btsquiz.common.LevelConstants
import com.justcircleprod.btsquiz.data.AppRepository
import com.justcircleprod.btsquiz.data.models.passedQuestion.PassedQuestion.Companion.toPassedQuestion
import com.justcircleprod.btsquiz.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.data.models.questions.ImageQuestion
import com.justcircleprod.btsquiz.data.models.questions.Question
import com.justcircleprod.btsquiz.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.data.models.questions.VideoQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: AppRepository,
    state: SavedStateHandle
) : ViewModel() {
    val isLoading = MutableLiveData(true)

    // value to prevent the observer from triggering when setting the default value
    var isFirstStart = true

    val userCoinsQuantity = repository.getUserCoinsQuantity()

    val levelId =
        state.get<Int>(QuizActivity.LEVEL_ARGUMENT_NAME) ?: LevelConstants.LEVEL_PASSED_QUESTIONS_ID

    val questionWorth = MutableLiveData(
        when (levelId) {
            LevelConstants.LEVEL_PASSED_QUESTIONS_ID -> LevelConstants.LEVEL_PASSED_QUESTIONS_QUESTION_WORTH
            LevelConstants.LEVEL_1_ID -> LevelConstants.LEVEL_1_QUESTION_WORTH
            LevelConstants.LEVEL_2_ID -> LevelConstants.LEVEL_2_QUESTION_WORTH
            LevelConstants.LEVEL_3_ID -> LevelConstants.LEVEL_3_QUESTION_WORTH
            LevelConstants.LEVEL_4_ID -> LevelConstants.LEVEL_4_QUESTION_WORTH
            LevelConstants.LEVEL_5_ID -> LevelConstants.LEVEL_5_QUESTION_WORTH
            LevelConstants.LEVEL_6_ID -> LevelConstants.LEVEL_6_QUESTION_WORTH
            LevelConstants.LEVEL_7_ID -> LevelConstants.LEVEL_7_QUESTION_WORTH
            else -> LevelConstants.LEVEL_PASSED_QUESTIONS_QUESTION_WORTH
        }
    )

    var earnedCoins = 0
    var correctlyAnsweredQuestionsCount = 0

    private var questions = mutableListOf<Question>()
    var questionsCount = 0

    private var questionPosition = 0

    // the current question or a sign of the end of questions is placed here
    val question = MutableLiveData<Question?>()
    var rightAnswer = ""

    val withoutQuizHintsState = repository.getWithoutQuizHintsState()

    // for current question
    val hint5050Used = MutableLiveData(false)
    val hintCorrectAnswerUsed = MutableLiveData(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            when (levelId) {
                LevelConstants.LEVEL_PASSED_QUESTIONS_ID -> {
                    questions.addAll(repository.getRandomPassedQuestions())
                }

                LevelConstants.LEVEL_1_ID -> {
                    questions.addAll(repository.getRandomQuestions(300))
                }

                LevelConstants.LEVEL_2_ID -> {
                    questions.addAll(repository.getRandomQuestions(350))
                }

                LevelConstants.LEVEL_3_ID -> {
                    questions.addAll(repository.getRandomQuestions(400))
                }

                LevelConstants.LEVEL_4_ID -> {
                    questions.addAll(repository.getRandomQuestions(450))
                }

                LevelConstants.LEVEL_5_ID -> {
                    questions.addAll(repository.getRandomQuestions(500))
                }

                LevelConstants.LEVEL_6_ID -> {
                    questions.addAll(repository.getRandomQuestions(550))
                }

                LevelConstants.LEVEL_7_ID -> {
                    questions.addAll(repository.getRandomQuestions(600))
                }
            }

            onLoadingEnd()
        }
    }

    // a method that updates the loading status
    // and shuffles questions when the desired number of questions is reached
    private fun onLoadingEnd() {
        questions = questions.shuffled() as MutableList<Question>
        questionsCount = questions.size
        isLoading.postValue(false)
    }

    private fun setRightAnswer(question: Question) {
        rightAnswer = listOf(
            question.firstOption,
            question.secondOption,
            question.thirdOption,
            question.fourthOption,
        )[question.answerNum - 1]
    }

    private fun getQuestionWithShuffledOptions(question: Question): Question {
        val options = listOf(
            question.firstOption,
            question.secondOption,
            question.thirdOption,
            question.fourthOption,
        ).shuffled()

        return when (question) {
            is TextQuestion -> {
                question.copy(
                    firstOption = options[0],
                    secondOption = options[1],
                    thirdOption = options[2],
                    fourthOption = options[3]
                )
            }

            is ImageQuestion -> {
                question.copy(
                    firstOption = options[0],
                    secondOption = options[1],
                    thirdOption = options[2],
                    fourthOption = options[3]
                )
            }

            is AudioQuestion -> {
                question.copy(
                    firstOption = options[0],
                    secondOption = options[1],
                    thirdOption = options[2],
                    fourthOption = options[3]
                )
            }

            is VideoQuestion -> {
                question.copy(
                    firstOption = options[0],
                    secondOption = options[1],
                    thirdOption = options[2],
                    fourthOption = options[3]
                )
            }

            else -> question
        }
    }

    fun setQuestionOnCurrentPosition() {
        if (questionPosition < questions.size) {
            setRightAnswer(questions[questionPosition])
            question.value = getQuestionWithShuffledOptions(questions[questionPosition])
        } else {
            question.value = null
        }
    }

    fun setQuestionOnNextPosition() {
        questionPosition++
        hint5050Used.value = false
        hintCorrectAnswerUsed.value = false

        if (questionPosition < questions.size) {
            setRightAnswer(questions[questionPosition])
            question.value = getQuestionWithShuffledOptions(questions[questionPosition])
        } else {
            question.value = null
        }
    }

    fun checkAnswer(userAnswer: String): Boolean {
        return if (userAnswer == rightAnswer) {
            onRightAnswer()
            true
        } else {
            false
        }
    }

    fun onRightAnswer() {
        viewModelScope.launch {
            if (levelId != LevelConstants.LEVEL_PASSED_QUESTIONS_ID) {
                val passedQuestion = question.value!!.toPassedQuestion()

                withContext(Dispatchers.IO) {
                    repository.insertPassedQuestion(passedQuestion)
                }
            }

            earnedCoins += questionWorth.value!!
            correctlyAnsweredQuestionsCount++

            if (levelId != LevelConstants.LEVEL_PASSED_QUESTIONS_ID) {
                withContext(Dispatchers.IO) {
                    repository.addLevelProgress(levelId, 1)
                }
            }
        }

    }

    fun useHint5050() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.subtractUserCoins(CoinConstants.HINT_50_50_PRICE)
            hint5050Used.postValue(true)
        }
    }

    fun useHintCorrectAnswer() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.subtractUserCoins(CoinConstants.HINT_CORRECT_ANSWER_PRICE)
            hintCorrectAnswerUsed.postValue(true)
        }
    }
}