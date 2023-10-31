package com.justcircleprod.btsquiz.ui.main

import androidx.lifecycle.ViewModel
import com.justcircleprod.btsquiz.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    val isIntroductionShown = repository.isIntroductionShown()

    suspend fun setIntroductionShown() {
        withContext(Dispatchers.IO) {
            repository.setIntroductionShown()
        }
    }
}