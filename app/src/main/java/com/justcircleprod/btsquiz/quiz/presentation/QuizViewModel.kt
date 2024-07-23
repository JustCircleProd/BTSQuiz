package com.justcircleprod.btsquiz.quiz.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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

    val isLoading = MutableLiveData(true)

    val userCoinsQuantity = coinRepository.getUserCoinsQuantity().asLiveData()

    val levelId = state.get<Int>(QuizActivity.LEVEL_ARGUMENT_NAME)!!

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

    var questionPosition = -1

    // the current question or a sign of the end of questions is placed here
    val question = MutableLiveData<Question?>(null)
    var correctAnswer = ""

    val withoutQuizHints = settingRepository.getWithoutQuizHintsState().asLiveData()

    // for current question
    val hint5050UsedWithOptionsToShow =
        MutableLiveData<Pair<Boolean, List<String>>>(Pair(false, listOf()))
    val hintCorrectAnswerUsed = MutableLiveData(false)

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

            questions = questions.shuffled() as MutableList<Question>
            questionsCount = questions.size
            isLoading.postValue(false)
        }
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
        if (questionPosition < questions.size) {
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
            hint5050UsedWithOptionsToShow.postValue(Pair(false, listOf()))
            hintCorrectAnswerUsed.postValue(false)

            if (questionPosition < questions.size) {
                setCorrectAnswer(questions[questionPosition])
                question.postValue(getQuestionWithShuffledOptions(questions[questionPosition]))
            } else {
                savePassedQuestions()
                question.postValue(null)
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

        earnedCoins += questionWorth.value!!
        correctlyAnsweredQuestionsCount++
    }

    fun useHint5050(options: List<String>) {
        viewModelScope.launch {
            var optionsToShow = options.toMutableList()
            optionsToShow.remove(correctAnswer)
            optionsToShow = optionsToShow.shuffled().toMutableList().take(2).toMutableList()

            coinRepository.subtractUserCoins(CoinConstants.HINT_50_50_PRICE)
            hint5050UsedWithOptionsToShow.postValue(Pair(true, optionsToShow))
        }
    }

    fun useHintCorrectAnswer() {
        viewModelScope.launch {
            coinRepository.subtractUserCoins(CoinConstants.HINT_CORRECT_ANSWER_PRICE)
            hintCorrectAnswerUsed.postValue(true)
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