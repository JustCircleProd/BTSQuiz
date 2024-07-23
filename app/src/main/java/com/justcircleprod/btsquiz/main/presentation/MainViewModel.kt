package com.justcircleprod.btsquiz.main.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val settingRepository: SettingRepository) :
    ViewModel() {

    val isIntroductionShown = settingRepository.isIntroductionShown().asLiveData()

    val shouldStartIntroductionActivity = MutableLiveData(false)

    fun setIntroductionShown() {
        viewModelScope.launch {
            settingRepository.setIntroductionShown()
            shouldStartIntroductionActivity.postValue(true)
        }
    }
}