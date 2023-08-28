package com.justcircleprod.btsquiz.ui.levels

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
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.common.LevelConstants
import com.justcircleprod.btsquiz.databinding.ActivityLevelsBinding
import com.justcircleprod.btsquiz.ui.compensationReceivedDialog.CompensationReceivedDialog
import com.justcircleprod.btsquiz.ui.levels.unlockLevelDialog.UnlockLevelConfirmationDialog
import com.justcircleprod.btsquiz.ui.levels.unlockLevelDialog.UnlockLevelConfirmationDialogCallback
import com.justcircleprod.btsquiz.ui.main.MainActivity
import com.justcircleprod.btsquiz.ui.quiz.QuizActivity
import com.justcircleprod.btsquiz.ui.watchRewardedAdConfirmationDialog.WatchRewardedAdConfirmationDialog
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LevelsActivity : AppCompatActivity(), UnlockLevelConfirmationDialogCallback {
    private lateinit var binding: ActivityLevelsBinding
    private val viewModel: LevelsViewModel by viewModels()

    private lateinit var levelUnlockedPlayer: MediaPlayer
    private var levelUnlockedPlayerPrepared = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelsBinding.inflate(layoutInflater)

        enableAnimations()

        initAd()

        onBackPressedDispatcher.addCallback { startMainActivity() }
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        setCoinsQuantityCollector()

        setCompensationReceivedObserver()

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

    override fun onUnlockButtonClicked(levelId: Int, levelPrice: Int) {
        viewModel.unlockLevel(levelId, levelPrice)
        playLevelUnlockedSoundAndAnimation(levelId)
    }

    private fun enableAnimations() {
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.userCoinsLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

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
        val adUnitId =
            String(Base64.decode("Ui1NLTI0NjM5MTktNQ==", Base64.DEFAULT), Charsets.UTF_8)

        binding.bannerAdView.setAdUnitId(adUnitId)
        binding.bannerAdView.setAdSize(AdSize.stickySize(this, 300))

        loadAd()
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        binding.bannerAdView.setBannerAdEventListener(object : BannerAdEventListener {
            override fun onAdLoaded() {
                binding.bannerAdView.visibility = View.VISIBLE

                // to update ad every n seconds
                object : CountDownTimer(36000, 36000) {
                    override fun onTick(mills: Long) {}

                    override fun onFinish() {
                        loadAd()
                    }
                }.start()
            }

            override fun onAdFailedToLoad(p0: AdRequestError) {}

            override fun onAdClicked() {}

            override fun onLeftApplication() {}

            override fun onReturnedToApplication() {}

            override fun onImpression(p0: ImpressionData?) {}
        })

        binding.bannerAdView.loadAd(adRequest)
    }

    private fun setCompensationReceivedObserver() {
        viewModel.compensationReceived.observe(this) {
            lifecycleScope.launch {
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
                val userCoinsQuantity = it?.toInt() ?: 0

                binding.coinsQuantity.text =
                    getString(R.string.levels_users_coins_quantity, userCoinsQuantity)
            }
        }
    }

    private fun setupLevelPassedQuestionsCard() {
        viewModel.passedQuestionsCount.observe(this) { passedQuestionsCount ->
            if (passedQuestionsCount > 0) {
                binding.levelPassedQuestionsCard.setOnClickListener {
                    startQuizActivity(LevelConstants.LEVEL_PASSED_QUESTIONS_ID)
                }
                binding.levelPassedQuestionsCard.visibility = View.VISIBLE
                binding.levelPassedQuestionsProgressTv.text = passedQuestionsCount.toString()
            } else {
                binding.levelPassedQuestionsCard.setOnClickListener(null)
                binding.levelPassedQuestionsCard.visibility = View.GONE
            }
        }
    }

    private fun setupLevel1Card() {
        binding.level1Card.setOnClickListener {
            startQuizActivity(LevelConstants.LEVEL_1_ID)
        }

        viewModel.level1Progress.observe(this) {
            binding.level1ProgressIndicator.progress = it.progress
            binding.level1ProgressIndicator.max = it.questionsQuantity

            binding.level1ProgressTv.text =
                getString(R.string.level_progress, it.progress, it.questionsQuantity)
        }
    }

    private fun setLevel2InfoObserver() {
        viewModel.level2Info.observe(this) { lockedLevel ->
            if (!lockedLevel.isOpened) {
                binding.level2Card.setOnClickListener {
                    tryToUnlockLevel(LevelConstants.LEVEL_2_ID, lockedLevel.price)
                }

                binding.level2IconLock.visibility = View.VISIBLE
                binding.level2ProgressTv.text =
                    getString(R.string.open_for_n_coins, lockedLevel.price)
                binding.level2ProgressCoinsIcon.visibility = View.VISIBLE

                return@observe
            }

            binding.level2IconLock.visibility = View.GONE
            binding.level2ProgressCoinsIcon.visibility = View.GONE

            if (!viewModel.level2Progress.hasActiveObservers()) {
                viewModel.level2Progress.observe(this) { levelProgress ->
                    binding.level2Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_2_ID) }

                    binding.level2ProgressIndicator.progress = levelProgress.progress
                    binding.level2ProgressIndicator.max = levelProgress.questionsQuantity

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

    private fun setLevel3InfoObserver() {
        viewModel.level3Info.observe(this) { lockedLevel ->
            if (!lockedLevel.isOpened) {
                binding.level3Card.setOnClickListener {
                    tryToUnlockLevel(LevelConstants.LEVEL_3_ID, lockedLevel.price)
                }

                binding.level3IconLock.visibility = View.VISIBLE
                binding.level3ProgressTv.text =
                    getString(R.string.open_for_n_coins, lockedLevel.price)
                binding.level3ProgressCoinsIcon.visibility = View.VISIBLE

                return@observe
            }

            binding.level3IconLock.visibility = View.GONE
            binding.level3ProgressCoinsIcon.visibility = View.GONE

            if (!viewModel.level3Progress.hasActiveObservers()) {
                viewModel.level3Progress.observe(this) { levelProgress ->
                    binding.level3Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_3_ID) }

                    binding.level3ProgressIndicator.progress = levelProgress.progress
                    binding.level3ProgressIndicator.max = levelProgress.questionsQuantity

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

    private fun setLevel4ProgressObserver() {
        viewModel.level4Info.observe(this) { lockedLevel ->
            if (!lockedLevel.isOpened) {
                binding.level4Card.setOnClickListener {
                    tryToUnlockLevel(LevelConstants.LEVEL_4_ID, lockedLevel.price)
                }

                binding.level4IconLock.visibility = View.VISIBLE
                binding.level4ProgressTv.text =
                    getString(R.string.open_for_n_coins, lockedLevel.price)
                binding.level4ProgressCoinsIcon.visibility = View.VISIBLE

                return@observe
            }

            binding.level4IconLock.visibility = View.GONE
            binding.level4ProgressCoinsIcon.visibility = View.GONE

            if (!viewModel.level4Progress.hasActiveObservers()) {
                viewModel.level4Progress.observe(this) { levelProgress ->
                    binding.level4Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_4_ID) }

                    binding.level4ProgressIndicator.progress = levelProgress.progress
                    binding.level4ProgressIndicator.max = levelProgress.questionsQuantity

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

    private fun setLevel5ProgressObserver() {
        viewModel.level5Info.observe(this) { lockedLevel ->
            if (!lockedLevel.isOpened) {
                binding.level5Card.setOnClickListener {
                    tryToUnlockLevel(LevelConstants.LEVEL_5_ID, lockedLevel.price)
                }

                binding.level5IconLock.visibility = View.VISIBLE
                binding.level5ProgressTv.text =
                    getString(R.string.open_for_n_coins, lockedLevel.price)
                binding.level5ProgressCoinsIcon.visibility = View.VISIBLE

                return@observe
            }

            binding.level5IconLock.visibility = View.GONE
            binding.level5ProgressCoinsIcon.visibility = View.GONE

            if (!viewModel.level5Progress.hasActiveObservers()) {
                viewModel.level5Progress.observe(this) { levelProgress ->
                    binding.level5Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_5_ID) }

                    binding.level5ProgressIndicator.progress = levelProgress.progress
                    binding.level5ProgressIndicator.max = levelProgress.questionsQuantity

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

    private fun setLevel6ProgressObserver() {
        viewModel.level6Info.observe(this) { lockedLevel ->
            if (!lockedLevel.isOpened) {
                binding.level6Card.setOnClickListener {
                    tryToUnlockLevel(LevelConstants.LEVEL_6_ID, lockedLevel.price)
                }

                binding.level6IconLock.visibility = View.VISIBLE

                binding.level6ProgressTv.text =
                    getString(R.string.open_for_n_coins, lockedLevel.price)
                binding.level6ProgressCoinsIcon.visibility = View.VISIBLE

                return@observe
            }

            binding.level6IconLock.visibility = View.GONE
            binding.level6ProgressCoinsIcon.visibility = View.GONE

            if (!viewModel.level6Progress.hasActiveObservers()) {
                viewModel.level6Progress.observe(this) { levelProgress ->
                    binding.level6Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_6_ID) }

                    binding.level6ProgressIndicator.progress = levelProgress.progress
                    binding.level6ProgressIndicator.max = levelProgress.questionsQuantity

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

    private fun setLevel7ProgressObserver() {
        viewModel.level7Info.observe(this) { lockedLevel ->
            if (!lockedLevel.isOpened) {
                binding.level7Card.setOnClickListener {
                    tryToUnlockLevel(LevelConstants.LEVEL_7_ID, lockedLevel.price)
                }

                binding.level7IconLock.visibility = View.VISIBLE
                binding.level7ProgressTv.text =
                    getString(R.string.open_for_n_coins, lockedLevel.price)
                binding.level7ProgressCoinsIcon.visibility = View.VISIBLE

                return@observe
            }

            binding.level7IconLock.visibility = View.GONE
            binding.level7ProgressCoinsIcon.visibility = View.GONE

            if (!viewModel.level7Progress.hasActiveObservers()) {
                viewModel.level7Progress.observe(this) { levelProgress ->
                    binding.level7Card.setOnClickListener { startQuizActivity(LevelConstants.LEVEL_7_ID) }

                    binding.level7ProgressIndicator.progress = levelProgress.progress
                    binding.level7ProgressIndicator.max = levelProgress.questionsQuantity

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

    override fun onDestroy() {
        super.onDestroy()

        if (levelUnlockedPlayerPrepared) {
            levelUnlockedPlayer.release()
        }
    }
}