package com.justcircleprod.btsquiz.ui.results

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.justcircleprod.btsquiz.data.AppRepository
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(repository: AppRepository) : ViewModel() {
    var scores = repository.getScores()
}