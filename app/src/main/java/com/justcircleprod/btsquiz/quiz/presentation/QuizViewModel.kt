package com.justcircleprod.btsquiz.quiz.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.data.constants.LevelConstants
import com.justcircleprod.btsquiz.core.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.core.data.models.passedQuestion.PassedQuestion.Companion.toPassedQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.ImageQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.Question
import com.justcircleprod.btsquiz.core.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LevelProgressRepository
import com.justcircleprod.btsquiz.core.domain.repositories.PassedQuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.QuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    settingRepository: SettingRepository,
    private val passedQuestionRepository: PassedQuestionRepository,
    private val questionRepository: QuestionRepository,
    private val levelProgressRepository: LevelProgressRepository,
    state: SavedStateHandle
) : ViewModel() {

    val isLoading = MutableStateFlow(true)

    val userCoinsQuantity = coinRepository.getUserCoinsQuantity()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val levelId = state.get<Int>(QuizActivity.LEVEL_ARGUMENT_NAME)!!

    val questionWorth = MutableStateFlow(
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

    var questionPosition = -1

    // the current question or a sign of the end of questions is placed here
    val question = MutableStateFlow<Question?>(null)
    var correctAnswer = ""

    val withoutQuizHints = settingRepository.getWithoutQuizHintsState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "NOT_INITIALIZED")

    // for current question
    val hint5050Used = MutableStateFlow(false)
    val hintCorrectAnswerUsed = MutableStateFlow(false)

    private val passedQuestions = mutableListOf<PassedQuestion>()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            when (levelId) {
                LevelConstants.LEVEL_PASSED_QUESTIONS_ID -> {
                    questions.addAll(passedQuestionRepository.getRandomPassedQuestions())
                }

                LevelConstants.LEVEL_1_ID -> {
                    questions.addAll(questionRepository.getRandomQuestions(300))
                }

                LevelConstants.LEVEL_2_ID -> {
                    questions.addAll(questionRepository.getRandomQuestions(350))
                }

                LevelConstants.LEVEL_3_ID -> {
                    questions.addAll(questionRepository.getRandomQuestions(400))
                }

                LevelConstants.LEVEL_4_ID -> {
                    questions.addAll(questionRepository.getRandomQuestions(450))
                }

                LevelConstants.LEVEL_5_ID -> {
                    questions.addAll(questionRepository.getRandomQuestions(500))
                }

                LevelConstants.LEVEL_6_ID -> {
                    questions.addAll(questionRepository.getRandomQuestions(550))
                }

                LevelConstants.LEVEL_7_ID -> {
                    questions.addAll(questionRepository.getRandomQuestions(600))
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
        isLoading.value = false
    }

    private fun setCorrectAnswer(question: Question) {
        correctAnswer = listOf(
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
        if (questionPosition + 1 < questions.size) {
            // If this is the first call, shuffle the question options,
            // otherwise they are already shuffled
            if (questionPosition == -1) {
                questionPosition = 0
                question.value = getQuestionWithShuffledOptions(questions[questionPosition])
            }

            setCorrectAnswer(questions[questionPosition])
        } else {
            question.value = null
        }
    }

    fun setQuestionOnNextPosition() {
        viewModelScope.launch {
            questionPosition++
            hint5050Used.value = false
            hintCorrectAnswerUsed.value = false

            if (questionPosition < questions.size) {
                setCorrectAnswer(questions[questionPosition])
                question.value = getQuestionWithShuffledOptions(questions[questionPosition])
            } else {
                savePassedQuestions()
                question.value = null
            }
        }
    }

    fun checkAnswer(userAnswer: String): Boolean {
        return if (userAnswer == correctAnswer) {
            onCorrectAnswer()
            true
        } else {
            false
        }
    }

    fun onCorrectAnswer() {
        if (levelId != LevelConstants.LEVEL_PASSED_QUESTIONS_ID) {
            val passedQuestion = question.value!!.toPassedQuestion()
            passedQuestions.add(passedQuestion)
        }

        earnedCoins += questionWorth.value
        correctlyAnsweredQuestionsCount++
    }

    fun useHint5050() {
        viewModelScope.launch {
            coinRepository.subtractUserCoins(CoinConstants.HINT_50_50_PRICE)
            hint5050Used.value = true
        }
    }

    fun useHintCorrectAnswer() {
        viewModelScope.launch {
            coinRepository.subtractUserCoins(CoinConstants.HINT_CORRECT_ANSWER_PRICE)
            hintCorrectAnswerUsed.value = true
        }
    }

    private suspend fun savePassedQuestions() {
        if (passedQuestions.size == 0) return

        passedQuestions.forEach {
            withContext(Dispatchers.IO) {
                passedQuestionRepository.insertPassedQuestion(it)
            }
        }

        withContext(Dispatchers.IO) {
            levelProgressRepository.addLevelProgress(levelId, passedQuestions.size)
        }
    }
}