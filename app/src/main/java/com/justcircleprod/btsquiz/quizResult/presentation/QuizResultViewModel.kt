package com.justcircleprod.btsquiz.quizResult.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            true
        )

    val earnedCoins =
        MutableStateFlow(state.get<Int>(QuizResultActivity.EARNED_COINS_ARGUMENT_NAME)!!)

    val areEarnedCoinsDoubled = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            coinRepository.addUserCoins(earnedCoins.value)
            areCoinsCalculating.value = false
        }
    }
}