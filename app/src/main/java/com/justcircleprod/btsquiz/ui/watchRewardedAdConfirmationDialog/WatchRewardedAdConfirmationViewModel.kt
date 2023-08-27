package com.justcircleprod.btsquiz.ui.watchRewardedAdConfirmationDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.common.CoinConstants
import com.justcircleprod.btsquiz.data.AppRepository
import com.justcircleprod.btsquiz.ui.common.RewardedAdState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchRewardedAdConfirmationViewModel @Inject constructor(private val repository: AppRepository) :
    ViewModel() {
    var rewardReceived = false
    val rewardedAdState = MutableLiveData<RewardedAdState>(RewardedAdState.UserNotAgreedYet)

    fun addReward() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUserCoins(CoinConstants.REWARDED_AD_WORTH)
        }
    }
}