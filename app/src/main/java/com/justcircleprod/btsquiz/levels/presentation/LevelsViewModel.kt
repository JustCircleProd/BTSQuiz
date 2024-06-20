package com.justcircleprod.btsquiz.levels.presentation

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.data.constants.LevelConstants
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LevelProgressRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LockedLevelRepository
import com.justcircleprod.btsquiz.core.domain.repositories.PassedQuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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

    val compensationReceived = MutableStateFlow(false)

    val userCoinsQuantity = coinRepository.getUserCoinsQuantity()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "NOT_INITIALIZED")

    val passedQuestionsCount = passedQuestionRepository.getPassedQuestionsCountFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val level1Progress = levelProgressRepository.getLevelProgressLiveData(LevelConstants.LEVEL_1_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val level2Info = lockedLevelRepository.getLockedLevelLiveData(LevelConstants.LEVEL_2_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    val level2Progress = levelProgressRepository.getLevelProgressLiveData(LevelConstants.LEVEL_2_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val level3Info = lockedLevelRepository.getLockedLevelLiveData(LevelConstants.LEVEL_3_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    val level3Progress = levelProgressRepository.getLevelProgressLiveData(LevelConstants.LEVEL_3_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val level4Info = lockedLevelRepository.getLockedLevelLiveData(LevelConstants.LEVEL_4_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    val level4Progress = levelProgressRepository.getLevelProgressLiveData(LevelConstants.LEVEL_4_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val level5Info = lockedLevelRepository.getLockedLevelLiveData(LevelConstants.LEVEL_5_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    val level5Progress = levelProgressRepository.getLevelProgressLiveData(LevelConstants.LEVEL_5_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val level6Info = lockedLevelRepository.getLockedLevelLiveData(LevelConstants.LEVEL_6_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    val level6Progress = levelProgressRepository.getLevelProgressLiveData(LevelConstants.LEVEL_6_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val level7Info = lockedLevelRepository.getLockedLevelLiveData(LevelConstants.LEVEL_7_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    val level7Progress = levelProgressRepository.getLevelProgressLiveData(LevelConstants.LEVEL_7_ID)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    // calculating initial coins quantity or compensation
    init {
        viewModelScope.launch(Dispatchers.IO) {
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

                    compensationReceived.value = true

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

                    compensationReceived.value = true

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
        viewModelScope.launch(Dispatchers.IO) {
            coinRepository.subtractUserCoins(levelPrice)
            lockedLevelRepository.unlockLevel(levelId)
        }
    }
}