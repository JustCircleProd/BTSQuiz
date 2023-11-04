package com.justcircleprod.btsquiz.ui.quizResult

import android.animation.LayoutTransition
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.justcircleprod.btsquiz.App
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.ActivityQuizResultBinding
import com.justcircleprod.btsquiz.ui.levels.LevelsActivity
import com.justcircleprod.btsquiz.ui.quiz.QuizActivity
import com.justcircleprod.btsquiz.ui.quizResult.doubleCoinsConfirmationDialog.DoubleCoinsConfirmationDialog
import com.justcircleprod.btsquiz.ui.quizResult.doubleCoinsConfirmationDialog.DoubleCoinsConfirmationDialogCallback
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoadListener
import com.yandex.mobile.ads.interstitial.InterstitialAdLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@AndroidEntryPoint
class QuizResultActivity : AppCompatActivity(), DoubleCoinsConfirmationDialogCallback {
    private lateinit var binding: ActivityQuizResultBinding
    private val viewModel: QuizResultViewModel by viewModels()

    private var interstitialAd: InterstitialAd? = null
    private var interstitialAdLoader: InterstitialAdLoader? = null
    private var isAdLoaded = false

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

        setLoadingObserver()
        workWithInterstitialAd()
        setEarnedCoinsObserver()

        setEarnedCoinsDoubledObserver()
        setOnDoubleCoinsBtnClickListener()

        setOnRepeatQuizBtnClickListener()
        setOnToLevelsBtnClicked()

        setContentView(binding.root)
    }

    private fun enableAnimations() {
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setLoadingObserver() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading.all { !it }) {
                onBackPressedDispatcher.addCallback { startLevelsActivity() }
                showResult()
                binding.loadLayout.visibility = View.GONE
                binding.contentLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun workWithInterstitialAd() {
        // if an ad has already been shown
        if (!viewModel.isLoading.value!![1]) {
            return
        }

        (application as App).onTestPassed()

        val showAd =
            (application as App).passedTestNum >= (application as App).passedTestNumForShowingAd
        if (!showAd) {
            viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
            return
        }

        interstitialAdLoader = InterstitialAdLoader(this).apply {
            setAdLoadListener(object : InterstitialAdLoadListener {
                override fun onAdLoaded(loadedInterstitialAd: InterstitialAd) {
                    interstitialAd = loadedInterstitialAd
                    isAdLoaded = true
                    showAd()
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
                    viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
                    destroyInterstitialAd()
                }
            })
        }

        val adUnitId =
            String(Base64.decode("Ui1NLTI0NjM5MTktMg==", Base64.DEFAULT), Charsets.UTF_8)
        val adRequestConfiguration = AdRequestConfiguration.Builder(adUnitId).build()

        interstitialAdLoader?.loadAd(adRequestConfiguration)

        // to limit the time to load an ad
        object : CountDownTimer(5000, 5000) {
            override fun onTick(mills: Long) {}

            override fun onFinish() {
                if (!isAdLoaded) {
                    interstitialAdLoader?.cancelLoading()
                    viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
                    destroyInterstitialAd()
                }
            }
        }.start()
    }

    private fun showAd() {
        interstitialAd?.apply {
            setAdEventListener(object : InterstitialAdEventListener {
                override fun onAdShown() {}

                override fun onAdFailedToShow(adError: AdError) {
                    viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
                    destroyInterstitialAd()
                }

                override fun onAdDismissed() {
                    viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
                    destroyInterstitialAd()
                }

                override fun onAdClicked() {}

                override fun onAdImpression(impressionData: ImpressionData?) {}
            })

            show(this@QuizResultActivity)
        }
    }

    private fun setEarnedCoinsObserver() {
        viewModel.earnedCoins.observe(this) {
            binding.earnedCoins.text = it.toString()
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

        lifecycleScope.launch(Dispatchers.IO) {
            resultPlayer.setDataSource(
                this@QuizResultActivity,
                Uri.parse("android.resource://$packageName/raw/$audioName")
            )
            resultPlayer.prepareAsync()
        }
    }

    private fun setEarnedCoinsDoubledObserver() {
        viewModel.earnedCoinsDoubled.observe(this) {
            binding.doubleCoinsBtn.visibility =
                if (it || viewModel.earnedCoins.value == 0) {
                    View.GONE
                } else {
                    View.VISIBLE
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

    override fun onSubmitReward() {
        viewModel.earnedCoinsDoubled.value = true
        val earnedCoins = viewModel.earnedCoins.value

        if (earnedCoins != null) {
            viewModel.earnedCoins.value = earnedCoins * 2
        }
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

    private fun destroyInterstitialAd() {
        interstitialAdLoader?.setAdLoadListener(null)
        interstitialAdLoader = null

        interstitialAd?.setAdEventListener(null)
        interstitialAd = null
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyInterstitialAd()

        if (::resultPlayer.isInitialized) {
            resultPlayer.release()
        }
    }
}