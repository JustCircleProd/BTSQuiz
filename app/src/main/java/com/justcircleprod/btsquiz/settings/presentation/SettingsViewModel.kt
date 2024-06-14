package com.justcircleprod.btsquiz.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.data.constants.LevelConstants
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LevelProgressRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LockedLevelRepository
import com.justcircleprod.btsquiz.core.domain.repositories.PassedQuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val coinRepository: CoinRepository,
    private val passedQuestionRepository: PassedQuestionRepository,
    private val lockedLevelRepository: LockedLevelRepository,
    private val levelProgressRepository: LevelProgressRepository
) : ViewModel() {

    val withoutQuizHints = settingRepository.getWithoutQuizHintsState()

    fun updateWithoutQuizHintsState(state: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.editWithoutQuizHintsState(state)
        }
    }

    fun resetProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            coinRepository.editUserCoinsQuantity(CoinConstants.INITIAL_COINS_QUANTITY)
            passedQuestionRepository.deleteAllPassedQuestions()

            lockedLevelRepository.lockLevel(LevelConstants.LEVEL_2_ID)
            lockedLevelRepository.lockLevel(LevelConstants.LEVEL_3_ID)
            lockedLevelRepository.lockLevel(LevelConstants.LEVEL_4_ID)
            lockedLevelRepository.lockLevel(LevelConstants.LEVEL_5_ID)
            lockedLevelRepository.lockLevel(LevelConstants.LEVEL_6_ID)
            lockedLevelRepository.lockLevel(LevelConstants.LEVEL_7_ID)

            levelProgressRepository.resetLevelProgress(LevelConstants.LEVEL_1_ID)
            levelProgressRepository.resetLevelProgress(LevelConstants.LEVEL_2_ID)
            levelProgressRepository.resetLevelProgress(LevelConstants.LEVEL_3_ID)
            levelProgressRepository.resetLevelProgress(LevelConstants.LEVEL_4_ID)
            levelProgressRepository.resetLevelProgress(LevelConstants.LEVEL_5_ID)
            levelProgressRepository.resetLevelProgress(LevelConstants.LEVEL_6_ID)
            levelProgressRepository.resetLevelProgress(LevelConstants.LEVEL_7_ID)
        }
    }

    suspend fun getPassedQuestionCount() = passedQuestionRepository.getPassedQuestionsCount()
}