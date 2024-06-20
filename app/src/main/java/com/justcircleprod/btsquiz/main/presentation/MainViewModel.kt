package com.justcircleprod.btsquiz.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val settingRepository: SettingRepository) :
    ViewModel() {

    val isIntroductionShown = settingRepository.isIntroductionShown()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "NOT_INITIALIZED")

    suspend fun setIntroductionShown() {
        withContext(Dispatchers.IO) {
            settingRepository.setIntroductionShown()
        }
    }
}