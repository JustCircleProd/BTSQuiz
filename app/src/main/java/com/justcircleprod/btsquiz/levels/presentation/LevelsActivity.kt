package com.justcircleprod.btsquiz.levels.presentation

import android.animation.Animator
import android.animation.LayoutTransition
import android.content.Intent
import android.graphics.Color
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
import androidx.recyclerview.widget.DividerItemDecoration
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.compensationReceived.presentation.CompensationReceivedDialog
import com.justcircleprod.btsquiz.databinding.ActivityLevelsBinding
import com.justcircleprod.btsquiz.levels.presentation.levelAdapter.LevelItemAdapter
import com.justcircleprod.btsquiz.levels.presentation.levelAdapter.LevelItemAdapterActions
import com.justcircleprod.btsquiz.main.presentation.MainActivity
import com.justcircleprod.btsquiz.quiz.presentation.QuizActivity
import com.justcircleprod.btsquiz.unlockLevel.presentation.UnlockLevelConfirmationDialog
import com.justcircleprod.btsquiz.watchRewardedAd.presentation.WatchRewardedAdConfirmationDialog
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@AndroidEntryPoint
class LevelsActivity : AppCompatActivity(), LevelItemAdapterActions {
    private lateinit var binding: ActivityLevelsBinding
    private val viewModel: LevelsViewModel by viewModels()

    private lateinit var levelUnlockedPlayer: MediaPlayer
    private var isLevelUnlockedPlayerPrepared = false
    private var isLevelUnlockedPlayerPlaying = false

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelsBinding.inflate(layoutInflater)

        enableAnimations()
        initLevelUnlockedPlayer()

        initAd()

        onBackPressedDispatcher.addCallback { startMainActivity() }
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        setCoinsQuantityCollector()
        setupRecyclerView()

        setCompensationReceivedCollector()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        refreshAdTimer?.start()
        if (isLevelUnlockedPlayerPlaying) {
            levelUnlockedPlayer.start()
        }
    }

    override fun onPause() {
        super.onPause()

        refreshAdTimer?.cancel()
        if (isLevelUnlockedPlayerPlaying) {
            levelUnlockedPlayer.pause()
        }
    }

    private fun enableAnimations() {
        binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.userCoinsQuantityLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun initLevelUnlockedPlayer() {
        levelUnlockedPlayer = MediaPlayer()

        levelUnlockedPlayer.setOnPreparedListener {
            isLevelUnlockedPlayerPrepared = true
            it.setOnPreparedListener(null)
        }

        levelUnlockedPlayer.setOnCompletionListener {
            isLevelUnlockedPlayerPlaying = false
        }

        levelUnlockedPlayer.setDataSource(
            this,
            Uri.parse("android.resource://$packageName/raw/level_unlocked")
        )
        levelUnlockedPlayer.prepareAsync()
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

                binding.line.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = LevelItemAdapter(
            levelItems = listOf(),
            levelItemAdapterActions = this
        )

        binding.levelsRecyclerView.adapter = adapter

        binding.levelsRecyclerView.itemAnimator = null

        val decoration = MaterialDividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            .apply {
                dividerColor = Color.TRANSPARENT
                setDividerThicknessResource(this@LevelsActivity, R.dimen.level_card_margin_between)
                isLastItemDecorated = false
            }

        binding.levelsRecyclerView.addItemDecoration(decoration)

        lifecycleScope.launch {
            viewModel.levelItems.collect {
                adapter.submitList(it)
            }
        }
    }

    override fun tryToUnlockLevel(
        levelId: Int,
        levelPrice: Int,
        confettiAnimationView: LottieAnimationView
    ) {
        lifecycleScope.launch {
            val userCoinsQuantity = viewModel.userCoinsQuantity.first()?.toInt() ?: return@launch

            if (userCoinsQuantity >= levelPrice) {
                supportFragmentManager.setFragmentResultListener(
                    UnlockLevelConfirmationDialog.SHOULD_UNLOCK_A_LEVEL_RESULT_KEY,
                    this@LevelsActivity
                ) { _, bundle ->

                    supportFragmentManager.clearFragmentResultListener(
                        UnlockLevelConfirmationDialog.SHOULD_UNLOCK_A_LEVEL_RESULT_KEY
                    )

                    if (bundle.getBoolean(UnlockLevelConfirmationDialog.SHOULD_UNLOCK_A_LEVEL_RESULT_KEY)) {
                        viewModel.unlockLevel(levelId, levelPrice)
                        startLevelUnlockedPlayerAndConfettiAnimation(confettiAnimationView)
                    }
                }

                UnlockLevelConfirmationDialog().show(supportFragmentManager, null)
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

    override fun startQuizActivity(levelId: Int) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra(QuizActivity.LEVEL_ARGUMENT_NAME, levelId)
        startActivity(intent)
        finish()
    }

    private fun startLevelUnlockedPlayerAndConfettiAnimation(confettiAnimationView: LottieAnimationView) {
        confettiAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                confettiAnimationView.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator) {
                confettiAnimationView.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animator) {}
        })

        if (isLevelUnlockedPlayerPrepared) {
            levelUnlockedPlayer.start()
            isLevelUnlockedPlayerPlaying = true

            confettiAnimationView.visibility = View.VISIBLE
            confettiAnimationView.playAnimation()
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

        if (isLevelUnlockedPlayerPrepared) {
            levelUnlockedPlayer.release()
        }
    }
}