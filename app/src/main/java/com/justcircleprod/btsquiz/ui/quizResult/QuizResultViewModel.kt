package com.justcircleprod.btsquiz.ui.quizResult

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.data.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val repository: AppRepository,
    state: SavedStateHandle
) : ViewModel() {
    // 0 - loading and calculating scores
    // 1 - working with interstitial ad
    val isLoading = MutableLiveData(listOf(true, true))

    val levelId = state.get<Int>(QuizResultActivity.LEVEL_ARGUMENT_NAME)!!

    val earnedCoins =
        MutableLiveData(state.get<Int>(QuizResultActivity.EARNED_COINS_ARGUMENT_NAME)!!)

    val earnedCoinsDoubled = MutableLiveData(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUserCoins(earnedCoins.value!!)

            isLoading.postValue(listOf(false, isLoading.value!![1]))
        }
    }
}