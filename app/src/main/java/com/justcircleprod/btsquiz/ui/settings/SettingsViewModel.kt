package com.justcircleprod.btsquiz.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.common.CoinConstants
import com.justcircleprod.btsquiz.common.LevelConstants
import com.justcircleprod.btsquiz.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    val withoutQuizHints = repository.getWithoutQuizHintsState()

    fun updateWithoutQuizHintsState(state: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editWithoutQuzHintsState(state)
        }
    }

    fun resetProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.editUserCoinsQuantity(CoinConstants.INITIAL_COINS_QUANTITY)
            repository.deleteAllPassedQuestions()

            repository.lockLevel(LevelConstants.LEVEL_2_ID)
            repository.lockLevel(LevelConstants.LEVEL_3_ID)
            repository.lockLevel(LevelConstants.LEVEL_4_ID)
            repository.lockLevel(LevelConstants.LEVEL_5_ID)
            repository.lockLevel(LevelConstants.LEVEL_6_ID)
            repository.lockLevel(LevelConstants.LEVEL_7_ID)

            repository.resetLevelProgress(LevelConstants.LEVEL_1_ID)
            repository.resetLevelProgress(LevelConstants.LEVEL_2_ID)
            repository.resetLevelProgress(LevelConstants.LEVEL_3_ID)
            repository.resetLevelProgress(LevelConstants.LEVEL_4_ID)
            repository.resetLevelProgress(LevelConstants.LEVEL_5_ID)
            repository.resetLevelProgress(LevelConstants.LEVEL_6_ID)
            repository.resetLevelProgress(LevelConstants.LEVEL_7_ID)
        }
    }

    suspend fun getPassedQuestionCount() = repository.getPassedQuestionsCount()
}