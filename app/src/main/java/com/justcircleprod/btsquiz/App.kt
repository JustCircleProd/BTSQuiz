package com.justcircleprod.btsquiz

import android.app.Application
import com.yandex.mobile.ads.common.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    // to determine the display of ads after each 3 passed the test
    var passedTestNum = 0
    val passedTestNumForShowingAd = 2

    override fun onCreate() {
        super.onCreate()

        MobileAds.initialize(this) { }
    }

    // increase passedTestNum
    // if TestNum > passedTestNumForShowingAd then reset to 1
    fun onTestPassed() {
        passedTestNum++

        if (passedTestNum > passedTestNumForShowingAd) {
            passedTestNum = 1
        }
    }
}