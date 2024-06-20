package com.justcircleprod.btsquiz.unlockLevel.presentation

interface UnlockLevelConfirmationDialogCallback {

    fun onUnlockButtonClicked(levelId: Int, levelPrice: Int)
}