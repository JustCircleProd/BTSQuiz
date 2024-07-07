package com.justcircleprod.btsquiz.watchRewardedAd.presentation

import android.animation.LayoutTransition
import android.app.Dialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.presentation.RewardedAdState
import com.justcircleprod.btsquiz.databinding.DialogWatchRewardedAdConfirmationBinding
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
class WatchRewardedAdConfirmationDialog : DialogFragment() {
    companion object {
        private const val MISSING_COINS_QUANTITY_NAME_ARGUMENT = "MISSING_COINS_QUANTITY"

        fun newInstance(missingCoinsQuantity: Int): WatchRewardedAdConfirmationDialog {
            return WatchRewardedAdConfirmationDialog().apply {
                arguments = Bundle().apply {
                    putInt(MISSING_COINS_QUANTITY_NAME_ARGUMENT, missingCoinsQuantity)
                }
            }
        }
    }

    private lateinit var binding: DialogWatchRewardedAdConfirmationBinding
    private val viewModel by viewModels<WatchRewardedAdConfirmationViewModel>()

    private var missingCoinsQuantity: Int? = null

    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null
    private var rewardedAdLoaded = false

    private lateinit var rewardReceivedPlayer: MediaPlayer
    private var isRewardReceivedPlayerPrepared = false
    private var isRewardReceivedPlayerPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        missingCoinsQuantity = arguments?.getInt(MISSING_COINS_QUANTITY_NAME_ARGUMENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogWatchRewardedAdConfirmationBinding.inflate(layoutInflater)

        enableAnimations()
        initRewardReceivedPlayer()
        setLoadingGif()
        setOnButtonsClickListeners()
        setRewardedAdStateCollector()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    override fun onResume() {
        super.onResume()

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
            it.setOnPreparedListener(null)
        }

        rewardReceivedPlayer.setOnCompletionListener {
            isRewardReceivedPlayerPlaying = false
            isRewardReceivedPlayerPrepared = false

            it.setOnCompletionListener(null)
            it.release()
        }

        rewardReceivedPlayer.setDataSource(
            requireContext(),
            Uri.parse("android.resource://${requireActivity().packageName}/raw/${R.raw.reward_received_sound}")
        )
        rewardReceivedPlayer.prepareAsync()
    }

    private fun setLoadingGif() {
        Glide
            .with(this)
            .load(R.drawable.loading_animation)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.loadingGif)
    }

    private fun setOnButtonsClickListeners() {
        binding.titleCancelBtn.setOnClickListener { dialog?.cancel() }
        binding.cancelBtn.setOnClickListener { dialog?.cancel() }

        binding.watchAdBtn.setOnClickListener {
            viewModel.rewardedAdState.value = RewardedAdState.Loading
            loadRewardedAd()
        }

        binding.submitRewardResultBtn.setOnClickListener { dismiss() }
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
            String(Base64.decode("Ui1NLTI0NjM5MTktMw==", Base64.DEFAULT), Charsets.UTF_8)
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
                    viewModel.addReward()
                    viewModel.rewardReceived = true
                }
            })

            show(requireActivity())
        }
    }

    private fun setRewardedAdStateCollector() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rewardedAdState.collect {
                    when (it) {
                        RewardedAdState.UserNotAgreedYet -> {
                            binding.title.text = getString(R.string.not_enough_coins_title)

                            binding.loadingLayout.visibility = View.GONE
                            binding.rewardResultLayout.visibility = View.GONE
                            binding.rewardResultQuantityLayout.visibility = View.GONE

                            if (missingCoinsQuantity != null) {
                                binding.missingCoinsQuantity.text = missingCoinsQuantity.toString()
                            }
                            binding.rewardedAdWorth.text =
                                CoinConstants.REWARDED_AD_WORTH.toString()

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
                            binding.title.text =
                                getString(R.string.failed_to_load_rewarded_ad_title)

                            binding.questionLayout.visibility = View.GONE
                            binding.loadingLayout.visibility = View.GONE
                            binding.rewardResultQuantityLayout.visibility = View.GONE

                            binding.rewardResultTv.text =
                                getString(R.string.failed_to_load_rewarded_ad)
                            binding.submitRewardResultBtn.setText(R.string.confirm)

                            binding.rewardResultLayout.visibility = View.VISIBLE
                        }

                        RewardedAdState.RewardReceived -> {
                            binding.title.text = getString(R.string.reward_received_title)

                            binding.rewardResultTv.text = getString(R.string.reward_received)
                            binding.rewardResultQuantity.text =
                                CoinConstants.REWARDED_AD_WORTH.toString()
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
    }
}