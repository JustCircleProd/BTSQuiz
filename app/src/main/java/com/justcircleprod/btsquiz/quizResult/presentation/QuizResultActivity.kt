package com.justcircleprod.btsquiz.quizResult.presentation

import android.animation.LayoutTransition
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.justcircleprod.btsquiz.App
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.ActivityQuizResultBinding
import com.justcircleprod.btsquiz.doubleCoins.presentation.DoubleCoinsConfirmationDialog
import com.justcircleprod.btsquiz.doubleCoins.presentation.DoubleCoinsConfirmationDialogCallback
import com.justcircleprod.btsquiz.levels.presentation.LevelsActivity
import com.justcircleprod.btsquiz.quiz.presentation.QuizActivity
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class QuizResultActivity : AppCompatActivity(), DoubleCoinsConfirmationDialogCallback {
    private lateinit var binding: ActivityQuizResultBinding
    private val viewModel: QuizResultViewModel by viewModels()

    private var bannerAdView: BannerAdView? = null
    private val adSize: BannerAdSize
        get() {
            // Calculate the width of the ad, taking into account the padding in the ad container.
            var adWidthPixels = binding.bannerAdView.width
            if (adWidthPixels == 0) {
                // If the ad hasn't been laid out, default to the full screen width
                adWidthPixels = resources.displayMetrics.widthPixels
            }
            val adWidth =
                ((adWidthPixels - resources.getDimensionPixelSize(R.dimen.banner_ad_horizontal_margin) * 2) / resources.displayMetrics.density).roundToInt()
            val maxAdHeight =
                (resources.getDimensionPixelSize(R.dimen.banner_ad_height) / resources.displayMetrics.density).roundToInt()

            return BannerAdSize.inlineSize(this, adWidth, maxAdHeight)
        }

    private var bannerAdLoadingTimer: CountDownTimer? = null
    private var refreshAdTimer: CountDownTimer? = null

    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoadingTimer: CountDownTimer? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null

    private lateinit var resultPlayer: MediaPlayer

    companion object {
        const val LEVEL_ARGUMENT_NAME = "LEVEL"
        const val EARNED_COINS_ARGUMENT_NAME = "EARNED_COINS"
        const val CORRECTLY_ANSWERED_QUESTIONS_COUNT_ARGUMENT_NAME =
            "CORRECTLY_ANSWERED_QUESTIONS_COUNT"
        const val QUESTIONS_COUNT_ARGUMENT_NAME = "QUESTIONS_COUNT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)

        enableAnimations()
        setLoadingGif()

        setLoadingCollector()

        initBannerAd()
        workWithInterstitialAd()

        setEarnedCoinsCollector()
        setEarnedCoinsDoubledCollector()
        setOnDoubleCoinsBtnClickListener()

        setOnRepeatQuizBtnClickListener()
        setOnToLevelsBtnClicked()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        refreshAdTimer?.start()
    }

    override fun onPause() {
        super.onPause()
        refreshAdTimer?.cancel()
    }

    private fun enableAnimations() {
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setLoadingGif() {
        Glide
            .with(this)
            .load(R.drawable.loading)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.loadingGif)
    }

    private fun initBannerAd() {
        binding.bannerAdView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.bannerAdView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                binding.bannerAdView.apply {
                    setAdSize(this@QuizResultActivity.adSize)

                    val adUnitId =
                        String(
                            Base64.decode("Ui1NLTI0NjM5MTktNg==", Base64.DEFAULT),
                            Charsets.UTF_8
                        )
                    binding.bannerAdView.setAdUnitId(adUnitId)
                }

                bannerAdView = loadBannerAd()

                // to limit the time for first loading of an ad
                bannerAdLoadingTimer = object : CountDownTimer(1750, 1750) {
                    override fun onTick(mills: Long) {}

                    override fun onFinish() {
                        viewModel.isBannerAdLoading.value = false
                    }
                }.start()
            }
        })
    }

    private fun loadBannerAd(): BannerAdView {
        return binding.bannerAdView.apply {
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {
                    // If this callback occurs after the activity is destroyed, you
                    // must call destroy and return or you may get a memory leak.
                    // Note `isDestroyed` is a method on Activity.
                    if (isDestroyed) {
                        bannerAdView?.destroy()
                        return
                    }

                    // to update ad every n seconds
                    refreshAdTimer?.start()?.also {
                        return
                    }

                    refreshAdTimer = object : CountDownTimer(30000, 30000) {
                        override fun onTick(mills: Long) {}

                        override fun onFinish() {
                            bannerAdView = loadBannerAd()
                        }
                    }.start()
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    viewModel.isBannerAdLoading.value = false
                    destroyBannerAd()
                }

                override fun onAdClicked() {}

                override fun onLeftApplication() {}

                override fun onReturnedToApplication() {}

                override fun onImpression(impressionData: ImpressionData?) {}
            })

            loadAd(AdRequest.Builder().build())
        }
    }

    private fun setLoadingCollector() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (!isLoading) {
                    onBackPressedDispatcher.addCallback { startLevelsActivity() }
                    showResult()
                    binding.loadLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun workWithInterstitialAd() {
        // if an ad has already been shown
        if (!viewModel.isInterstitialAdLoading.value) {
            return
        }

        (application as App).onTestPassed()

        val showAd =
            (application as App).passedTestNum >= (application as App).passedTestNumForShowingAd

        if (!showAd) {
            viewModel.isInterstitialAdLoading.value = false
            return
        }

        interstitialAdLoader = InterstitialAdLoader(this).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@QuizResultActivity.interstitialAd = interstitialAd
                    interstitialAdLoadingTimer?.cancel()
                    showAd()
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    viewModel.isInterstitialAdLoading.value = false
                    destroyInterstitialAd()
                }
            })
        }

        val adUnitId =
            String(Base64.decode("Ui1NLTI0NjM5MTktMg==", Base64.DEFAULT), Charsets.UTF_8)
        val adRequestConfiguration = AdRequestConfiguration.Builder(adUnitId).build()

        interstitialAdLoader?.loadAd(adRequestConfiguration)

        // to limit the time to load an ad
        interstitialAdLoadingTimer = object : CountDownTimer(5000, 5000) {
            override fun onTick(mills: Long) {}

            override fun onFinish() {
                interstitialAdLoader?.cancelLoading()
                viewModel.isInterstitialAdLoading.value = false
                destroyInterstitialAd()
            }
        }.start()
    }

    private fun showAd() {
        interstitialAd?.apply {
            setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {}

                override fun onAdFailedToShow(adError: AdError) {
                    viewModel.isInterstitialAdLoading.value = false
                    destroyInterstitialAd()
                }

                override fun onAdDismissed() {
                    viewModel.isInterstitialAdLoading.value = false
                    destroyInterstitialAd()
                }

                override fun onAdClicked() {}

                override fun onAdImpression(impressionData: ImpressionData?) {}
            })

            show(this@QuizResultActivity)
        }
    }

    private fun setEarnedCoinsCollector() {
        lifecycleScope.launch {
            viewModel.earnedCoins.collect {
                binding.earnedCoins.text = it.toString()
            }
        }
    }

    private fun showResult() {
        val correctlyAnsweredQuestionsCount =
            intent.extras!!.getInt(CORRECTLY_ANSWERED_QUESTIONS_COUNT_ARGUMENT_NAME)

        val questionsCount = intent.extras!!.getInt(QUESTIONS_COUNT_ARGUMENT_NAME)

        when {
            questionsCount == 1 -> {
                if (correctlyAnsweredQuestionsCount == 0) {
                    onBadResult()
                } else {
                    onBestResult()
                }
            }

            correctlyAnsweredQuestionsCount > (questionsCount * 0.66).roundToInt() -> {
                onBestResult()
            }

            correctlyAnsweredQuestionsCount in (questionsCount * 0.33).roundToInt()..(questionsCount * 0.66).roundToInt() -> {
                onGoodResult()
            }

            else -> {
                onBadResult()
            }
        }
    }

    private fun onBestResult() {
        binding.congratulationText.text =
            resources.getStringArray(R.array.texts_for_best_result).toList().shuffled()[0]

        val imageResource = listOf(
            R.drawable.best_result,
            R.drawable.best_result_2,
            R.drawable.best_result_3
        ).shuffled()[0]

        Glide
            .with(this)
            .load(imageResource)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.congratulationImage)

        playResultSound("best_result")
    }

    private fun onGoodResult() {
        binding.congratulationText.text =
            resources.getStringArray(R.array.texts_for_good_result).toList().shuffled()[0]

        val imageResource = listOf(
            R.drawable.good_result,
            R.drawable.good_result_2,
            R.drawable.good_result_3
        ).shuffled()[0]

        Glide
            .with(this)
            .load(imageResource)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.congratulationImage)

        playResultSound("good_result")
    }

    private fun onBadResult() {
        binding.congratulationText.text =
            resources.getStringArray(R.array.texts_for_bad_result).toList().shuffled()[0]

        val imageResource = listOf(
            R.drawable.bad_result,
            R.drawable.bad_result_2,
            R.drawable.bad_result_3
        ).shuffled()[0]

        Glide
            .with(this)
            .load(imageResource)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.congratulationImage)

        playResultSound("bad_result")
    }

    private fun playResultSound(audioName: String) {
        resultPlayer = MediaPlayer()

        resultPlayer.setOnPreparedListener {
            resultPlayer.start()
        }


        resultPlayer.setDataSource(
            this@QuizResultActivity,
            Uri.parse("android.resource://$packageName/raw/$audioName")
        )
        resultPlayer.prepareAsync()
    }

    private fun setEarnedCoinsDoubledCollector() {
        lifecycleScope.launch {
            viewModel.earnedCoinsDoubled.collect {
                binding.doubleCoinsBtn.visibility =
                    if (it || viewModel.earnedCoins.value == 0) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }
            }
        }
    }

    private fun setOnDoubleCoinsBtnClickListener() {
        binding.doubleCoinsBtn.setOnClickListener {
            val earnedCoins = intent.extras?.getInt(EARNED_COINS_ARGUMENT_NAME)

            if (earnedCoins != null) {
                DoubleCoinsConfirmationDialog.newInstance(earnedCoins)
                    .show(supportFragmentManager, null)
            }
        }
    }

    override fun onCoinsDoublingConfirmed() {
        viewModel.earnedCoinsDoubled.value = true
        val earnedCoins = viewModel.earnedCoins.value

        viewModel.earnedCoins.value = earnedCoins * 2
    }

    private fun setOnRepeatQuizBtnClickListener() {
        binding.continueQuizBtn.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(
                QuizActivity.LEVEL_ARGUMENT_NAME, this.intent.extras!!.getInt(
                    LEVEL_ARGUMENT_NAME
                )
            )
            startActivity(intent)
            finish()
        }
    }

    private fun setOnToLevelsBtnClicked() {
        binding.toLevelsBtn.setOnClickListener {
            startLevelsActivity()
        }
    }

    private fun startLevelsActivity() {
        val intent = Intent(this, LevelsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun destroyBannerAd() {
        bannerAdLoadingTimer?.cancel()
        bannerAdView = null

        refreshAdTimer?.cancel()
        refreshAdTimer = null

        bannerAdView?.destroy()
        bannerAdView = null
    }

    private fun destroyInterstitialAd() {
        interstitialAdLoadingTimer?.cancel()
        interstitialAdLoadingTimer = null

        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null

        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyBannerAd()
        destroyInterstitialAd()

        if (::resultPlayer.isInitialized) {
            resultPlayer.release()
        }
    }
}