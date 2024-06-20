package com.justcircleprod.btsquiz.levels.presentation

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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.compensationReceived.presentation.CompensationReceivedDialog
import com.justcircleprod.btsquiz.core.data.constants.LevelConstants
import com.justcircleprod.btsquiz.core.presentation.animateProgress
import com.justcircleprod.btsquiz.databinding.ActivityLevelsBinding
import com.justcircleprod.btsquiz.main.presentation.MainActivity
import com.justcircleprod.btsquiz.quiz.presentation.QuizActivity
import com.justcircleprod.btsquiz.unlockLevel.presentation.UnlockLevelConfirmationDialog
import com.justcircleprod.btsquiz.unlockLevel.presentation.UnlockLevelConfirmationDialogCallback
import com.justcircleprod.btsquiz.watchRewardedAd.presentation.WatchRewardedAdConfirmationDialog
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@AndroidEntryPoint
class LevelsActivity : AppCompatActivity(), UnlockLevelConfirmationDialogCallback {
    private lateinit var binding: ActivityLevelsBinding
    private val viewModel: LevelsViewModel by viewModels()

    private lateinit var levelUnlockedPlayer: MediaPlayer
    private var levelUnlockedPlayerPrepared = false

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
    private var refreshAdTimer: CountDownTimer? = null

    private var level2ProgressCollectionJob: Job? = null
    private var level3ProgressCollectionJob: Job? = null
    private var level4ProgressCollectionJob: Job? = null
    private var level5ProgressCollectionJob: Job? = null
    private var level6ProgressCollectionJob: Job? = null
    private var level7ProgressCollectionJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelsBinding.inflate(layoutInflater)

        enableAnimations()

        initAd()

        onBackPressedDispatcher.addCallback { startMainActivity() }
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        setCoinsQuantityCollector()

        setCompensationReceivedCollector()

        setupLevelPassedQuestionsCard()
        setupLevel1Card()
        setLevel2InfoObserver()
        setLevel3InfoObserver()
        setLevel4ProgressObserver()
        setLevel5ProgressObserver()
        setLevel6ProgressObserver()
        setLevel7ProgressObserver()

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

    override fun onUnlockButtonClicked(levelId: Int, levelPrice: Int) {
        viewModel.unlockLevel(levelId, levelPrice)
        playLevelUnlockedSoundAndAnimation(levelId)
    }

    private fun enableAnimations() {
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.userCoinsQuantityLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.levelPassedQuestionsCardLayout.layoutTransition.enableTransitionType(
            LayoutTransition.CHANGING
        )
        binding.level1CardLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.level2NameLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.level2ProgressTextLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.level3NameLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.level3ProgressTextLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.level4NameLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.level4ProgressTextLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.level5NameLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.level5ProgressTextLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.level6NameLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.level6ProgressTextLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.level7NameLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.level7ProgressTextLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun initAd() {
        binding.bannerAdView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.bannerAdView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                binding.bannerAdView.apply {
                    setAdSize(this@LevelsActivity.adSize)

                    val adUnitId =
                        String(
                            Base64.decode("Ui1NLTI0NjM5MTktNQ==", Base64.DEFAULT),
                            Charsets.UTF_8
                        )
                    binding.bannerAdView.setAdUnitId(adUnitId)
                }

                bannerAdView = loadBannerAd()
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
                    if (refreshAdTimer != null) {
                        refreshAdTimer?.start()
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

    private fun setCompensationReceivedCollector() {
        lifecycleScope.launch {
            // false never hit this
            viewModel.compensationReceived.takeWhile { it }.collect {
                val coinsQuantity = viewModel.userCoinsQuantity.first()?.toInt()

                if (it && coinsQuantity != null) {
                    CompensationReceivedDialog.newInstance(coinsQuantity)
                        .show(supportFragmentManager, null)

                    viewModel.compensationReceived.value = false
                }
            }
        }
    }

    private fun setCoinsQuantityCollector() {
        lifecycleScope.launch {
            viewModel.userCoinsQuantity.collect {
                if (it == null || !it.isDigitsOnly()) return@collect

                binding.userCoinsQuantity.text =
                    getString(R.string.levels_users_coins_quantity, it.toInt())

                binding.userCoinsQuantityLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setupLevelPassedQuestionsCard() {
        lifecycleScope.launch {
            viewModel.passedQuestionsCount.collect {
                if (it == null) return@collect

                if (it > 0) {
                    binding.levelPassedQuestionsCard.setOnClickListener {
                        startQuizActivity(LevelConstants.LEVEL_PASSED_QUESTIONS_ID)
                    }
                    binding.levelPassedQuestionsCard.visibility = View.VISIBLE
                    binding.levelPassedQuestionsProgressTv.text = it.toString()
                } else {
                    binding.levelPassedQuestionsCard.setOnClickListener(null)
                    binding.levelPassedQuestionsCard.visibility = View.GONE
                }
            }
        }
    }

    private fun setupLevel1Card() {
        binding.level1Card.setOnClickListener {
            startQuizActivity(LevelConstants.LEVEL_1_ID)
        }

        lifecycleScope.launch {
            viewModel.level1Progress.collect {
                if (it == null) return@collect

                binding.level1ProgressIndicator.max = it.questionsQuantity
                binding.level1ProgressIndicator.animateProgress(0, it.progress)

                binding.level1ProgressTv.text =
                    getString(R.string.level_progress, it.progress, it.questionsQuantity)
            }
        }

    }

    private fun setLevel2InfoObserver() {
        lifecycleScope.launch {
            viewModel.level2Info.collect { lockedLevel ->
                if (lockedLevel == null) return@collect

                if (!lockedLevel.isOpened) {
                    binding.level2Card.setOnClickListener {
                        tryToUnlockLevel(LevelConstants.LEVEL_2_ID, lockedLevel.price)
                    }

                    binding.level2IconLock.visibility = View.VISIBLE
                    binding.level2ProgressTv.text =
                        getString(R.string.open_for_n_coins, lockedLevel.price)
                    binding.level2ProgressCoinsIcon.visibility = View.VISIBLE

                    return@collect
                }

                binding.level2IconLock.visibility = View.GONE
                binding.level2ProgressCoinsIcon.visibility = View.GONE

                if (level2ProgressCollectionJob != null) return@collect

                level2ProgressCollectionJob = lifecycleScope.launch {
                    viewModel.level2Progress.collect collect2@{ levelProgress ->
                        if (levelProgress == null) return@collect2

                        binding.level2Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_2_ID) }

                        binding.level2ProgressIndicator.max = levelProgress.questionsQuantity
                        binding.level2ProgressIndicator.animateProgress(0, levelProgress.progress)

                        binding.level2ProgressTv.text =
                            getString(
                                R.string.level_progress,
                                levelProgress.progress,
                                levelProgress.questionsQuantity
                            )
                    }
                }
            }
        }
    }

    private fun setLevel3InfoObserver() {
        lifecycleScope.launch {
            viewModel.level3Info.collect { lockedLevel ->
                if (lockedLevel == null) return@collect

                if (!lockedLevel.isOpened) {
                    binding.level3Card.setOnClickListener {
                        tryToUnlockLevel(LevelConstants.LEVEL_3_ID, lockedLevel.price)
                    }

                    binding.level3IconLock.visibility = View.VISIBLE
                    binding.level3ProgressTv.text =
                        getString(R.string.open_for_n_coins, lockedLevel.price)
                    binding.level3ProgressCoinsIcon.visibility = View.VISIBLE

                    return@collect
                }

                binding.level3IconLock.visibility = View.GONE
                binding.level3ProgressCoinsIcon.visibility = View.GONE

                if (level3ProgressCollectionJob != null) return@collect

                level3ProgressCollectionJob = lifecycleScope.launch {
                    viewModel.level3Progress.collect collect2@{ levelProgress ->
                        if (levelProgress == null) return@collect2

                        binding.level3Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_3_ID) }

                        binding.level3ProgressIndicator.max = levelProgress.questionsQuantity
                        binding.level3ProgressIndicator.animateProgress(0, levelProgress.progress)

                        binding.level3ProgressTv.text =
                            getString(
                                R.string.level_progress,
                                levelProgress.progress,
                                levelProgress.questionsQuantity
                            )
                    }
                }
            }
        }
    }

    private fun setLevel4ProgressObserver() {
        lifecycleScope.launch {
            viewModel.level4Info.collect { lockedLevel ->
                if (lockedLevel == null) return@collect

                if (!lockedLevel.isOpened) {
                    binding.level4Card.setOnClickListener {
                        tryToUnlockLevel(LevelConstants.LEVEL_4_ID, lockedLevel.price)
                    }

                    binding.level4IconLock.visibility = View.VISIBLE
                    binding.level4ProgressTv.text =
                        getString(R.string.open_for_n_coins, lockedLevel.price)
                    binding.level4ProgressCoinsIcon.visibility = View.VISIBLE

                    return@collect
                }

                binding.level4IconLock.visibility = View.GONE
                binding.level4ProgressCoinsIcon.visibility = View.GONE

                if (level4ProgressCollectionJob != null) return@collect

                level4ProgressCollectionJob = lifecycleScope.launch {
                    viewModel.level4Progress.collect collect2@{ levelProgress ->
                        if (levelProgress == null) return@collect2

                        binding.level4Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_4_ID) }

                        binding.level4ProgressIndicator.max = levelProgress.questionsQuantity
                        binding.level4ProgressIndicator.animateProgress(0, levelProgress.progress)

                        binding.level4ProgressTv.text =
                            getString(
                                R.string.level_progress,
                                levelProgress.progress,
                                levelProgress.questionsQuantity
                            )
                    }
                }
            }
        }
    }

    private fun setLevel5ProgressObserver() {
        lifecycleScope.launch {
            viewModel.level5Info.collect { lockedLevel ->
                if (lockedLevel == null) return@collect

                if (!lockedLevel.isOpened) {
                    binding.level5Card.setOnClickListener {
                        tryToUnlockLevel(LevelConstants.LEVEL_5_ID, lockedLevel.price)
                    }

                    binding.level5IconLock.visibility = View.VISIBLE
                    binding.level5ProgressTv.text =
                        getString(R.string.open_for_n_coins, lockedLevel.price)
                    binding.level5ProgressCoinsIcon.visibility = View.VISIBLE

                    return@collect
                }

                binding.level5IconLock.visibility = View.GONE
                binding.level5ProgressCoinsIcon.visibility = View.GONE

                if (level5ProgressCollectionJob != null) return@collect

                level5ProgressCollectionJob = lifecycleScope.launch {
                    viewModel.level5Progress.collect collect2@{ levelProgress ->
                        if (levelProgress == null) return@collect2

                        binding.level5Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_5_ID) }

                        binding.level5ProgressIndicator.max = levelProgress.questionsQuantity
                        binding.level5ProgressIndicator.animateProgress(0, levelProgress.progress)

                        binding.level5ProgressTv.text =
                            getString(
                                R.string.level_progress,
                                levelProgress.progress,
                                levelProgress.questionsQuantity
                            )
                    }
                }
            }
        }
    }

    private fun setLevel6ProgressObserver() {
        lifecycleScope.launch {
            viewModel.level6Info.collect { lockedLevel ->
                if (lockedLevel == null) return@collect

                if (!lockedLevel.isOpened) {
                    binding.level6Card.setOnClickListener {
                        tryToUnlockLevel(LevelConstants.LEVEL_6_ID, lockedLevel.price)
                    }

                    binding.level6IconLock.visibility = View.VISIBLE
                    binding.level6ProgressTv.text =
                        getString(R.string.open_for_n_coins, lockedLevel.price)
                    binding.level6ProgressCoinsIcon.visibility = View.VISIBLE

                    return@collect
                }

                binding.level6IconLock.visibility = View.GONE
                binding.level6ProgressCoinsIcon.visibility = View.GONE

                if (level6ProgressCollectionJob != null) return@collect

                level6ProgressCollectionJob = lifecycleScope.launch {
                    viewModel.level6Progress.collect collect2@{ levelProgress ->
                        if (levelProgress == null) return@collect2

                        binding.level6Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_6_ID) }

                        binding.level6ProgressIndicator.max = levelProgress.questionsQuantity
                        binding.level6ProgressIndicator.animateProgress(0, levelProgress.progress)

                        binding.level6ProgressTv.text =
                            getString(
                                R.string.level_progress,
                                levelProgress.progress,
                                levelProgress.questionsQuantity
                            )
                    }
                }
            }
        }
    }

    private fun setLevel7ProgressObserver() {
        lifecycleScope.launch {
            viewModel.level7Info.collect { lockedLevel ->
                if (lockedLevel == null) return@collect

                if (!lockedLevel.isOpened) {
                    binding.level7Card.setOnClickListener {
                        tryToUnlockLevel(LevelConstants.LEVEL_7_ID, lockedLevel.price)
                    }

                    binding.level7IconLock.visibility = View.VISIBLE
                    binding.level7ProgressTv.text =
                        getString(R.string.open_for_n_coins, lockedLevel.price)
                    binding.level7ProgressCoinsIcon.visibility = View.VISIBLE

                    return@collect
                }

                binding.level7IconLock.visibility = View.GONE
                binding.level7ProgressCoinsIcon.visibility = View.GONE

                if (level7ProgressCollectionJob != null) return@collect

                level7ProgressCollectionJob = lifecycleScope.launch {
                    viewModel.level7Progress.collect collect2@{ levelProgress ->
                        if (levelProgress == null) return@collect2

                        binding.level7Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_7_ID) }

                        binding.level7ProgressIndicator.max = levelProgress.questionsQuantity
                        binding.level7ProgressIndicator.animateProgress(0, levelProgress.progress)

                        binding.level7ProgressTv.text =
                            getString(
                                R.string.level_progress,
                                levelProgress.progress,
                                levelProgress.questionsQuantity
                            )
                    }
                }
            }
        }
    }

    private fun tryToUnlockLevel(levelId: Int, levelPrice: Int) {
        lifecycleScope.launch {
            val userCoinsQuantity = viewModel.userCoinsQuantity.first()?.toInt() ?: return@launch

            if (userCoinsQuantity >= levelPrice) {
                UnlockLevelConfirmationDialog.newInstance(levelId, levelPrice)
                    .show(supportFragmentManager, null)
            } else {
                WatchRewardedAdConfirmationDialog.newInstance(levelPrice - userCoinsQuantity)
                    .show(supportFragmentManager, null)
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startQuizActivity(levelId: Int) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(QuizActivity.LEVEL_ARGUMENT_NAME, levelId)
        startActivity(intent)
        finish()
    }

    private fun playLevelUnlockedSoundAndAnimation(levelId: Int) {
        if (levelUnlockedPlayerPrepared) {
            levelUnlockedPlayer.start()
            playLevelUnlockedAnimation(levelId)
            return
        }

        levelUnlockedPlayer = MediaPlayer()

        levelUnlockedPlayer.setOnPreparedListener {
            levelUnlockedPlayerPrepared = true
            levelUnlockedPlayer.start()
            playLevelUnlockedAnimation(levelId)
        }

        levelUnlockedPlayer.setOnCompletionListener {
            when (levelId) {
                LevelConstants.LEVEL_2_ID -> {
                    binding.level2ConfettiAnimation.visibility = View.GONE
                    binding.level2ConfettiAnimation.pauseAnimation()
                }

                LevelConstants.LEVEL_3_ID -> {
                    binding.level3ConfettiAnimation.visibility = View.GONE
                    binding.level3ConfettiAnimation.pauseAnimation()
                }

                LevelConstants.LEVEL_4_ID -> {
                    binding.level4ConfettiAnimation.visibility = View.GONE
                    binding.level4ConfettiAnimation.pauseAnimation()
                }

                LevelConstants.LEVEL_5_ID -> {
                    binding.level5ConfettiAnimation.visibility = View.GONE
                    binding.level5ConfettiAnimation.pauseAnimation()
                }

                LevelConstants.LEVEL_6_ID -> {
                    binding.level6ConfettiAnimation.visibility = View.GONE
                    binding.level6ConfettiAnimation.pauseAnimation()
                }

                LevelConstants.LEVEL_7_ID -> {
                    binding.level7ConfettiAnimation.visibility = View.GONE
                    binding.level7ConfettiAnimation.pauseAnimation()
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            levelUnlockedPlayer.setDataSource(
                this@LevelsActivity,
                Uri.parse("android.resource://$packageName/raw/level_unlocked")
            )
            levelUnlockedPlayer.prepareAsync()
        }
    }

    private fun playLevelUnlockedAnimation(levelId: Int) {
        when (levelId) {
            LevelConstants.LEVEL_2_ID -> {
                binding.level2ConfettiAnimation.visibility = View.VISIBLE
                binding.level2ConfettiAnimation.playAnimation()
            }

            LevelConstants.LEVEL_3_ID -> {
                binding.level3ConfettiAnimation.visibility = View.VISIBLE
                binding.level3ConfettiAnimation.playAnimation()
            }

            LevelConstants.LEVEL_4_ID -> {
                binding.level4ConfettiAnimation.visibility = View.VISIBLE
                binding.level4ConfettiAnimation.playAnimation()
            }

            LevelConstants.LEVEL_5_ID -> {
                binding.level5ConfettiAnimation.visibility = View.VISIBLE
                binding.level5ConfettiAnimation.playAnimation()
            }

            LevelConstants.LEVEL_6_ID -> {
                binding.level6ConfettiAnimation.visibility = View.VISIBLE
                binding.level6ConfettiAnimation.playAnimation()
            }

            LevelConstants.LEVEL_7_ID -> {
                binding.level7ConfettiAnimation.visibility = View.VISIBLE
                binding.level7ConfettiAnimation.playAnimation()
            }
        }
    }

    private fun destroyBannerAd() {
        refreshAdTimer?.cancel()
        refreshAdTimer = null

        bannerAdView?.destroy()
        bannerAdView = null
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyBannerAd()

        if (levelUnlockedPlayerPrepared) {
            levelUnlockedPlayer.release()
        }
    }
}