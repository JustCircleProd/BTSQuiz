package com.justcircleprod.btsquiz.ui.introduction.introductionCardAdapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class IntroductionCardResources(
    @StringRes val title: Int,
    @StringRes val text: Int,
    @DrawableRes val gif: Int
)