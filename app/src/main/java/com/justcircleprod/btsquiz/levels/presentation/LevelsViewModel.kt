package com.justcircleprod.btsquiz.levels.presentation

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.data.constants.LevelConstants
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LevelProgressRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LockedLevelRepository
import com.justcircleprod.btsquiz.core.domain.repositories.PassedQuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.ScoreRepository
import com.justcircleprod.btsquiz.levels.presentation.levelAdapter.LevelItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    passedQuestionRepository: PassedQuestionRepository,
    levelProgressRepository: LevelProgressRepository,
    private val lockedLevelRepository: LockedLevelRepository,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    val compensationReceived = MutableLiveData(false)

    private val userCoinsQuantity = coinRepository.getUserCoinsQuantity()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "NOT_INITIALIZED")

    val userCoinsQuantityLiveData = coinRepository.getUserCoinsQuantity().asLiveData()

    private val passedQuestionsCount = passedQuestionRepository.getPassedQuestionsCountFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val lockedLevels = lockedLevelRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    private val levelProgress = levelProgressRepository.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

    // conversion to LevelItem for RecyclerView in presentation
    val levelItems = combine(
        lockedLevels,
        levelProgress,
        passedQuestionsCount
    ) { lockedLevels, levelProgress, passedQuestionsCount ->

        if (lockedLevels.isEmpty() || levelProgress.isEmpty() || passedQuestionsCount == null) {
            return@combine LevelItem.getPlaceholders()
        }

        val tempLevelItems = mutableListOf<LevelItem>()

        if (passedQuestionsCount != 0) {
            tempLevelItems.add(
                LevelItem(
                    levelId = LevelConstants.LEVEL_PASSED_QUESTIONS_ID,
                    isOpened = null,
                    price = 0,
                    progress = 0,
                    questionNumber = passedQuestionsCount
                )
            )
        }

        for (levelProgressItem in levelProgress) {
            val levelId = levelProgressItem.id
            val lockedLevel = lockedLevels.firstOrNull { levelProgressItem.id == it.id }

            tempLevelItems.add(
                LevelItem(
                    levelId = levelId,
                    isOpened = lockedLevel?.isOpened
                        ?: true,
                    price = lockedLevel?.price ?: 0,
                    progress = levelProgressItem.progress,
                    questionNumber = levelProgressItem.questionsQuantity
                )
            )
        }

        tempLevelItems.toList()
    }.asLiveData()


    // calculating initial coins quantity or compensation
    init {
        viewModelScope.launch(Dispatchers.Default) {
            userCoinsQuantity.collect { coinsQuantity ->
                if (coinsQuantity?.isDigitsOnly() == false || coinsQuantity != null) return@collect

                val passedQuestionsCount = passedQuestionRepository.getPassedQuestionsCount()

                // if there are passed questions,
                // then each question is counted according to the average worth
                // delete all passed questions after compensation received

                if (passedQuestionsCount != 0) {
                    coinRepository.editUserCoinsQuantity(
                        CoinConstants.INITIAL_COINS_QUANTITY + 5000 + LevelConstants.LEVEL_3_QUESTION_WORTH * passedQuestionsCount
                    )
                    passedQuestionRepository.deleteAllPassedQuestions()

                    compensationReceived.postValue(true)

                    cancel()
                    return@collect
                }

                // if there are no passed questions, then we roughly count coins according to the results
                // delete all passed questions after compensation received

                val scores = scoreRepository.getScores()

                if (scores.any { it.score > 0 }) {
                    val highScoresCount = scores.count { it.score > 1800 }
                    val averageScoresCount = scores.count { it.score in 901..1800 }
                    val lowScoresCount = scores.count { it.score <= 900 }

                    val initialCoins = when {
                        highScoresCount > 4 -> 20000
                        highScoresCount in 2..3 -> 16500
                        highScoresCount == 1 -> 12500
                        averageScoresCount > 5 -> 9500
                        averageScoresCount in 2..3 -> 7500
                        averageScoresCount == 1 -> 5500
                        lowScoresCount > 5 -> 4000
                        lowScoresCount in 2..3 -> 3000
                        lowScoresCount == 1 -> 2500
                        else -> CoinConstants.INITIAL_COINS_QUANTITY
                    }

                    coinRepository.editUserCoinsQuantity(initialCoins)
                    passedQuestionRepository.deleteAllPassedQuestions()

                    compensationReceived.postValue(true)

                    cancel()
                    return@collect
                }

                // if user has not played before the update with coins
                coinRepository.editUserCoinsQuantity(CoinConstants.INITIAL_COINS_QUANTITY)

                cancel()
            }
        }
    }

    fun unlockLevel(levelId: Int, levelPrice: Int) {
        viewModelScope.launch {
            coinRepository.subtractUserCoins(levelPrice)
            lockedLevelRepository.unlockLevel(levelId)
        }
    }
}