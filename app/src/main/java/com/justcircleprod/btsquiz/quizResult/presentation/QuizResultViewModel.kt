package com.justcircleprod.btsquiz.quizResult.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    state: SavedStateHandle
) : ViewModel() {

    val isBannerAdLoading = MutableStateFlow(true)
    val isInterstitialAdLoading = MutableStateFlow(true)

    private val areCoinsCalculating = MutableStateFlow(true)

    val isLoading =
        combine(
            areCoinsCalculating,
            isBannerAdLoading,
            isInterstitialAdLoading
        ) { areCoinsCalculating, isBannerAdLoading, isInterstitialAdLoading ->
            areCoinsCalculating || isBannerAdLoading || isInterstitialAdLoading
        }.asLiveData()

    val earnedCoins =
        MutableLiveData(state.get<Int>(QuizResultActivity.EARNED_COINS_ARGUMENT_NAME)!!)

    val areEarnedCoinsDoubled = MutableLiveData(false)

    init {
        viewModelScope.launch {
            coinRepository.addUserCoins(earnedCoins.value!!)
            areCoinsCalculating.value = false
        }
    }
}