package com.justcircleprod.btsquiz.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val settingRepository: SettingRepository) :
    ViewModel() {

    val isIntroductionShown = settingRepository.isIntroductionShown()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "NOT_INITIALIZED")

    val shouldStartIntroductionActivity = MutableStateFlow(false)

    fun setIntroductionShown() {
        viewModelScope.launch {
            settingRepository.setIntroductionShown()
            shouldStartIntroductionActivity.value = true
        }
    }
}