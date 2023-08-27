package com.justcircleprod.btsquiz.ui.levels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.common.CoinConstants
import com.justcircleprod.btsquiz.common.LevelConstants
import com.justcircleprod.btsquiz.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    val compensationReceived = MutableLiveData(false)

    val userCoinsQuantity = repository.getUserCoinsQuantity()

    val passedQuestionsCount = repository.getPassedQuestionsCountLiveData()

    val level1Progress = repository.getLevelProgressLiveData(LevelConstants.LEVEL_1_ID)

    val level2Info = repository.getLockedLevelLiveData(LevelConstants.LEVEL_2_ID)
    val level2Progress = repository.getLevelProgressLiveData(LevelConstants.LEVEL_2_ID)

    val level3Info = repository.getLockedLevelLiveData(LevelConstants.LEVEL_3_ID)
    val level3Progress = repository.getLevelProgressLiveData(LevelConstants.LEVEL_3_ID)

    val level4Info = repository.getLockedLevelLiveData(LevelConstants.LEVEL_4_ID)
    val level4Progress = repository.getLevelProgressLiveData(LevelConstants.LEVEL_4_ID)

    val level5Info = repository.getLockedLevelLiveData(LevelConstants.LEVEL_5_ID)
    val level5Progress = repository.getLevelProgressLiveData(LevelConstants.LEVEL_5_ID)

    val level6Info = repository.getLockedLevelLiveData(LevelConstants.LEVEL_6_ID)
    val level6Progress = repository.getLevelProgressLiveData(LevelConstants.LEVEL_6_ID)

    val level7Info = repository.getLockedLevelLiveData(LevelConstants.LEVEL_7_ID)
    val level7Progress = repository.getLevelProgressLiveData(LevelConstants.LEVEL_7_ID)

    init {
        // calculating initial coins or compensation
        viewModelScope.launch(Dispatchers.IO) {
            if (userCoinsQuantity.first() == null) {
                val passedQuestionsCount = repository.getPassedQuestionsCount()

                // if there are passed questions,
                // then each question is counted according to the average worth
                // delete all passed questions after compensation received

                if (passedQuestionsCount != 0) {
                    repository.editUserCoinsQuantity(
                         CoinConstants.INITIAL_COINS_QUANTITY + 5000 + LevelConstants.LEVEL_3_QUESTION_WORTH * passedQuestionsCount
                    )
                    repository.deleteAllPassedQuestions()

                    compensationReceived.postValue(true)

                    return@launch
                }

                // if there are no passed questions, then we roughly count coins according to the results
                // delete all passed questions after compensation received

                val scores = repository.getScores()

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

                    repository.editUserCoinsQuantity(initialCoins)
                    repository.deleteAllPassedQuestions()

                    compensationReceived.postValue(true)

                    return@launch
                }

                // if user has not played before the update with coins
                repository.editUserCoinsQuantity(CoinConstants.INITIAL_COINS_QUANTITY)
            }
        }
    }

    fun unlockLevel(levelId: Int, levelPrice: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.subtractUserCoins(levelPrice)
            repository.unlockLevel(levelId)
        }
    }
}