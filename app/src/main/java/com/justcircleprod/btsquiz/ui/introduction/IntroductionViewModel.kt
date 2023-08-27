package com.justcircleprod.btsquiz.ui.introduction

import androidx.lifecycle.ViewModel
import com.justcircleprod.btsquiz.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(private val repository: AppRepository) :
    ViewModel() {
    suspend fun setIntroductionShown() {
        withContext(Dispatchers.IO) {
            repository.setIntroductionShown()
        }
    }
}