package com.justcircleprod.btsquiz.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(settingRepository: SettingRepository) :
    ViewModel() {

    val isIntroductionShown = settingRepository.isIntroductionShown().asLiveData()
}