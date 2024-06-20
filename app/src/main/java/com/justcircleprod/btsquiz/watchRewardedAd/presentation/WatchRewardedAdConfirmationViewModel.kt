package com.justcircleprod.btsquiz.watchRewardedAd.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import com.justcircleprod.btsquiz.core.presentation.RewardedAdState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchRewardedAdConfirmationViewModel @Inject constructor(private val coinRepository: CoinRepository) :
    ViewModel() {

    var rewardReceived = false
    val rewardedAdState = MutableStateFlow<RewardedAdState>(RewardedAdState.UserNotAgreedYet)

    fun addReward() {
        viewModelScope.launch(Dispatchers.IO) {
            coinRepository.addUserCoins(CoinConstants.REWARDED_AD_WORTH)
        }
    }
}