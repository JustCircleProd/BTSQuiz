package com.justcircleprod.btsquiz.ui.levels.unlockLevelDialog

interface UnlockLevelConfirmationDialogCallback {
    fun onUnlockButtonClicked(levelId: Int, levelPrice: Int)
}