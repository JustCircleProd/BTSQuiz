package com.justcircleprod.btsquiz.ui.common

sealed class RewardedAdState {
    object UserNotAgreedYet : RewardedAdState()
    object Loading : RewardedAdState()
    object FailedToLoad : RewardedAdState()
    object RewardReceived : RewardedAdState()
}
