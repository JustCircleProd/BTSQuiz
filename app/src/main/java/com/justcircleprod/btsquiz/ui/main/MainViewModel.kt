package com.justcircleprod.btsquiz.ui.main

import androidx.lifecycle.ViewModel
import com.justcircleprod.btsquiz.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(repository: AppRepository) : ViewModel() {
    val isIntroductionShown = repository.isIntroductionShown()
}