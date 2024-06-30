package com.justcircleprod.btsquiz.doubleCoins.presentation

import android.animation.LayoutTransition
import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.core.presentation.RewardedAdState
import com.justcircleprod.btsquiz.databinding.DialogDoubleCoinsConfirmationBinding
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoubleCoinsConfirmationDialog : DialogFragment() {
    companion object {
        const val EARNED_COINS_NAME_ARGUMENT = "EARNED_COINS"

        fun newInstance(earnedCoins: Int): DoubleCoinsConfirmationDialog {
            return DoubleCoinsConfirmationDialog().apply {
                arguments = Bundle().apply {
                    putInt(EARNED_COINS_NAME_ARGUMENT, earnedCoins)
                }
            }
        }
    }

    private lateinit var binding: DialogDoubleCoinsConfirmationBinding
    private val viewModel by viewModels<DoubleCoinsConfirmationViewModel>()

    private var earnedCoins: Int? = null

    private lateinit var rewardReceivedPlayer: MediaPlayer
    private var isRewardReceivedPlayerPrepared = false
    private var isRewardReceivedPlayerPlaying = false

    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null
    private var rewardedAdLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        earnedCoins = arguments?.getInt(EARNED_COINS_NAME_ARGUMENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogDoubleCoinsConfirmationBinding.inflate(layoutInflater)

        enableAnimations()
        initRewardReceivedPlayer()
        setLoadingGif()
        setOnButtonsClickListeners()
        setRewardedAdStateCollector()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        if (viewModel.rewardedAdState.value == RewardedAdState.RewardReceived) {
            (activity as DoubleCoinsConfirmationDialogCallback).onCoinsDoublingConfirmed()
        }
    }

    override fun onStart() {
        super.onStart()

        if (isRewardReceivedPlayerPlaying) {
            rewardReceivedPlayer.start()
        }
    }

    override fun onPause() {
        super.onPause()

        if (isRewardReceivedPlayerPlaying) {
            rewardReceivedPlayer.pause()
        }
    }

    private fun enableAnimations() {
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun initRewardReceivedPlayer() {
        rewardReceivedPlayer = MediaPlayer()

        rewardReceivedPlayer.setOnPreparedListener {
            isRewardReceivedPlayerPrepared = true
            rewardReceivedPlayer.setOnPreparedListener(null)
        }

        rewardReceivedPlayer.setOnCompletionListener {
            isRewardReceivedPlayerPlaying = false
            rewardReceivedPlayer.setOnCompletionListener(null)
        }

        rewardReceivedPlayer.setDataSource(
            requireContext(),
            Uri.parse("android.resource://${requireActivity().packageName}/raw/reward_received")
        )
        rewardReceivedPlayer.prepareAsync()
    }

    private fun setLoadingGif() {
        Glide
            .with(this)
            .load(R.drawable.loading)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.loadingGif)
    }

    private fun setOnButtonsClickListeners() {
        binding.titleCancelBtn.setOnClickListener { dialog?.cancel() }
        binding.cancelBtn.setOnClickListener { dialog?.cancel() }

        binding.watchAdBtn.setOnClickListener {
            viewModel.rewardedAdState.value = RewardedAdState.Loading
            loadRewardedAd()
        }

        binding.submitRewardResultBtn.setOnClickListener {
            if (viewModel.rewardedAdState.value == RewardedAdState.RewardReceived) {
                (activity as DoubleCoinsConfirmationDialogCallback).onCoinsDoublingConfirmed()
            }
            dismiss()
        }
    }

    private fun loadRewardedAd() {
        rewardedAdLoader = RewardedAdLoader(requireContext()).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewarded: RewardedAd) {
                    rewardedAd = rewarded
                    rewardedAdLoaded = true
                    showAd()
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    viewModel.rewardedAdState.value = RewardedAdState.FailedToLoad
                    destroyRewardedAd()
                }
            })
        }

        val adUnitId =
            String(Base64.decode("Ui1NLTI0NjM5MTktNA==", Base64.DEFAULT), Charsets.UTF_8)
        val adRequestConfiguration = AdRequestConfiguration.Builder(adUnitId).build()

        rewardedAdLoader?.loadAd(adRequestConfiguration)

        // to limit the time to load an ad
        object : CountDownTimer(10000, 10000) {
            override fun onTick(mills: Long) {}

            override fun onFinish() {
                if (!rewardedAdLoaded) {
                    rewardedAdLoader?.cancelLoading()
                    viewModel.rewardedAdState.value = RewardedAdState.FailedToLoad
                    destroyRewardedAd()
                }
            }
        }.start()
    }

    private fun showAd() {
        rewardedAd?.apply {
            setAdEventListener(object : RewardedAdEventListener {
                override fun onAdShown() {}

                override fun onAdFailedToShow(adError: AdError) {
                    viewModel.rewardedAdState.value = if (viewModel.rewardReceived) {
                        RewardedAdState.RewardReceived
                    } else {
                        RewardedAdState.FailedToLoad
                    }

                    destroyRewardedAd()
                }

                override fun onAdDismissed() {
                    destroyRewardedAd()

                    if (viewModel.rewardReceived) {
                        viewModel.rewardedAdState.value = RewardedAdState.RewardReceived
                    } else {
                        dialog?.cancel()
                    }
                }

                override fun onAdClicked() {}

                override fun onAdImpression(impressionData: ImpressionData?) {}

                override fun onRewarded(reward: Reward) {
                    viewModel.doubleEarnedCoins()
                    viewModel.rewardReceived = true
                }
            })

            show(requireActivity())
        }
    }

    private fun setRewardedAdStateCollector() {
        lifecycleScope.launch {
            viewModel.rewardedAdState.collect {
                when (it) {
                    RewardedAdState.UserNotAgreedYet -> {
                        binding.title.text = getString(R.string.double_coins)

                        binding.loadingLayout.visibility = View.GONE
                        binding.rewardResultLayout.visibility = View.GONE
                        binding.rewardResultQuantityLayout.visibility = View.GONE

                        if (earnedCoins != null) {
                            binding.earnedCoinsToDouble.text =
                                getString(
                                    R.string.double_coins_quantity_with_placeholder,
                                    earnedCoins
                                )
                        }

                        binding.questionLayout.visibility = View.VISIBLE
                    }

                    RewardedAdState.Loading -> {
                        binding.title.text = getString(R.string.loading_ad_title)

                        binding.questionLayout.visibility = View.GONE
                        binding.rewardResultLayout.visibility = View.GONE
                        binding.rewardResultQuantityLayout.visibility = View.GONE

                        binding.loadingLayout.visibility = View.VISIBLE
                    }

                    RewardedAdState.FailedToLoad -> {
                        binding.title.text = getString(R.string.failed_to_load_rewarded_ad_title)

                        binding.questionLayout.visibility = View.GONE
                        binding.loadingLayout.visibility = View.GONE
                        binding.rewardResultQuantityLayout.visibility = View.GONE

                        binding.rewardResultTv.text = getString(R.string.failed_to_load_rewarded_ad)
                        binding.submitRewardResultBtn.setText(R.string.confirm)

                        binding.rewardResultLayout.visibility = View.VISIBLE
                    }

                    RewardedAdState.RewardReceived -> {
                        binding.title.text = getString(R.string.coins_doubled_title)

                        binding.rewardResultTv.text = getString(R.string.coins_doubled)
                        if (earnedCoins != null) {
                            binding.rewardResultQuantity.text = (earnedCoins!! * 2).toString()
                        }
                        binding.submitRewardResultBtn.setText(R.string.great)

                        binding.questionLayout.visibility = View.GONE
                        binding.loadingLayout.visibility = View.GONE

                        binding.rewardResultLayout.visibility = View.VISIBLE
                        binding.rewardResultQuantityLayout.visibility = View.VISIBLE

                        if (isRewardReceivedPlayerPrepared) {
                            rewardReceivedPlayer.start()
                            isRewardReceivedPlayerPlaying = true
                        }
                    }
                }
            }
        }
    }

    private fun destroyRewardedAd() {
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null

        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyRewardedAd()

        if (isRewardReceivedPlayerPrepared) {
            rewardReceivedPlayer.release()
        }
    }
}