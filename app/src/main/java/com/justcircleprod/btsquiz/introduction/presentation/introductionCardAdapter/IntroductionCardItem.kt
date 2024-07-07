package com.justcircleprod.btsquiz.introduction.presentation.introductionCardAdapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class IntroductionCardItem(
    @StringRes val titleStringResId: Int,
    @StringRes val textStringResId: Int,
    @DrawableRes val animationDrawableResId: Int
)