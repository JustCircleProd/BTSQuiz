package com.justcircleprod.btsquiz.main.presentation

import androidx.lifecycle.ViewModel
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val settingRepository: SettingRepository) :
    ViewModel() {
    val isIntroductionShown = settingRepository.isIntroductionShown()

    suspend fun setIntroductionShown() {
        withContext(Dispatchers.IO) {
            settingRepository.setIntroductionShown()
        }
    }
}