package com.justcircleprod.btsquiz.doubleCoins.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import com.justcircleprod.btsquiz.core.presentation.RewardedAdState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoubleCoinsConfirmationViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    state: SavedStateHandle
) : ViewModel() {

    var rewardReceived = false
    val rewardedAdState = MutableStateFlow<RewardedAdState>(RewardedAdState.UserNotAgreedYet)

    private val earnedCoins =
        state.get<Int>(DoubleCoinsConfirmationDialog.EARNED_COINS_NAME_ARGUMENT)!!

    fun doubleEarnedCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            coinRepository.addUserCoins(earnedCoins)
        }
    }
}