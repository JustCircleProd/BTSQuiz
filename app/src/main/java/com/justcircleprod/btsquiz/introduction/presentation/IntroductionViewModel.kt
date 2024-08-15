package com.justcircleprod.btsquiz.introduction.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel() {

    val shouldStartMainActivity = MutableLiveData(false)

    fun setIntroductionShown() {
        viewModelScope.launch {
            settingRepository.setIntroductionShown()
            shouldStartMainActivity.postValue(true)
        }
    }
}