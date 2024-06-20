package com.justcircleprod.btsquiz.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.PassedQuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    private val passedQuestionRepository: PassedQuestionRepository
) : ViewModel() {

    val withoutQuizHints = settingRepository.getWithoutQuizHintsState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "NOT_INITIALIZED")

    fun updateWithoutQuizHintsState(state: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingRepository.editWithoutQuizHintsState(state)
        }
    }

    suspend fun getPassedQuestionCount() = passedQuestionRepository.getPassedQuestionsCount()
}