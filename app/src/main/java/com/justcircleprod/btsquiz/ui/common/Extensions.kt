package com.justcircleprod.btsquiz.ui.common

import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

fun MaterialButton.disableWithTransparency() {
    this.isEnabled = false
    this.alpha = 0.4f
}

fun MaterialButton.enable() {
    this.isEnabled = true
    this.alpha = 1f
}

fun MaterialCardView.disableWithTransparency() {
    this.isEnabled = false
    this.alpha = 0.4f
}

fun MaterialCardView.enable() {
    this.isEnabled = true
    this.alpha = 1f
}