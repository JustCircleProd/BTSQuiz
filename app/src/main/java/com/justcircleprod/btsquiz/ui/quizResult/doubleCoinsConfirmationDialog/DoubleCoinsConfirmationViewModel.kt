package com.justcircleprod.btsquiz.ui.quizResult.doubleCoinsConfirmationDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justcircleprod.btsquiz.data.AppRepository
import com.justcircleprod.btsquiz.ui.common.RewardedAdState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoubleCoinsConfirmationViewModel @Inject constructor(
    private val repository: AppRepository,
    state: SavedStateHandle
) : ViewModel() {
    var rewardReceived = false
    val rewardedAdState = MutableLiveData<RewardedAdState>(RewardedAdState.UserNotAgreedYet)

    private val earnedCoins =
        state.get<Int>(DoubleCoinsConfirmationDialog.EARNED_COINS_NAME_ARGUMENT)!!

    fun doubleEarnedCoins() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUserCoins(earnedCoins)
        }
    }
}