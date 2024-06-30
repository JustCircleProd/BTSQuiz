package com.justcircleprod.btsquiz.levels.presentation.levelAdapter

import com.airbnb.lottie.LottieAnimationView

interface LevelItemAdapterActions {

    fun tryToUnlockLevel(levelId: Int, levelPrice: Int, confettiAnimationView: LottieAnimationView)

    fun startQuizActivity(levelId: Int)
}