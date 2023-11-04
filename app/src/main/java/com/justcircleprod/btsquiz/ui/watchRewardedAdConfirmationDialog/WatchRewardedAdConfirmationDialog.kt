package com.justcircleprod.btsquiz.ui.watchRewardedAdConfirmationDialog

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
import androidx.lifecycle.lifecycleScope
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.common.CoinConstants
import com.justcircleprod.btsquiz.databinding.DialogWatchRewardedAdConfirmationBinding
import com.justcircleprod.btsquiz.ui.common.RewardedAdState
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
import kotlinx.coroutines.Dispatchers
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        missingCoinsQuantity = arguments?.getInt(MISSING_COINS_QUANTITY_NAME_ARGUMENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogWatchRewardedAdConfirmationBinding.inflate(layoutInflater)

        enableAnimations()
        setOnButtonsClickListeners()
        setStateObserver()

        dialogBuilder.setView(binding.root).setCancelable(true)
        return dialogBuilder.create()
    }

    private fun enableAnimations() {
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setOnButtonsClickListeners() {
        binding.titleCancelBtn.setOnClickListener { dismiss() }
        binding.cancelBtn.setOnClickListener { dismiss() }

        binding.watchAdBtn.setOnClickListener {
            viewModel.rewardedAdState.value = RewardedAdState.Loading
            loadRewardedAd()
        }

        binding.submitRewardResultBtn.setOnClickListener { dismiss() }
    }

    private fun loadRewardedAd() {
        rewardedAdLoader = RewardedAdLoader(requireContext()).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(loadedRewardedAd: RewardedAd) {
                    rewardedAd = loadedRewardedAd
                    rewardedAdLoaded = true
                    showAd()
                }

                override fun onAdFailedToLoad(adRequestError: AdRequestError) {
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
                    if (viewModel.rewardReceived) {
                        viewModel.rewardedAdState.value = RewardedAdState.RewardReceived
                    }

                    destroyRewardedAd()
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

    private fun setStateObserver() {
        viewModel.rewardedAdState.observe(this) {
            binding.questionLayout.visibility = View.GONE
            binding.loadingLayout.visibility = View.GONE
            binding.rewardResultLayout.visibility = View.GONE
            binding.rewardResultQuantityLayout.visibility = View.GONE

            when (it) {
                RewardedAdState.UserNotAgreedYet -> {
                    binding.title.text = getString(R.string.not_enough_money_title)

                    binding.questionLayout.visibility = View.VISIBLE

                    if (missingCoinsQuantity != null) {
                        binding.missingCoinsQuantity.text = missingCoinsQuantity.toString()
                    }
                    binding.rewardedAdWorth.text = CoinConstants.REWARDED_AD_WORTH.toString()
                }

                RewardedAdState.Loading -> {
                    binding.title.text = getString(R.string.loading_ad_title)

                    binding.loadingLayout.visibility = View.VISIBLE
                }

                RewardedAdState.FailedToLoad -> {
                    binding.title.text = getString(R.string.failed_to_load_rewarded_ad_title)

                    binding.rewardResultLayout.visibility = View.VISIBLE

                    binding.rewardResultTv.text = getString(R.string.failed_to_load_rewarded_ad)
                }

                RewardedAdState.RewardReceived -> {
                    binding.title.text = getString(R.string.reward_received_title)

                    binding.rewardResultTv.text = getString(R.string.reward_received)
                    binding.rewardResultQuantity.text = CoinConstants.REWARDED_AD_WORTH.toString()

                    binding.rewardResultLayout.visibility = View.VISIBLE
                    binding.rewardResultQuantityLayout.visibility = View.VISIBLE

                    playRewardReceivedSound()
                }
            }
        }
    }

    private fun playRewardReceivedSound() {
        if (::rewardReceivedPlayer.isInitialized) return

        rewardReceivedPlayer = MediaPlayer()

        rewardReceivedPlayer.setOnPreparedListener {
            rewardReceivedPlayer.start()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            rewardReceivedPlayer.setDataSource(
                requireContext(),
                Uri.parse("android.resource://${requireActivity().packageName}/raw/reward_received")
            )
            rewardReceivedPlayer.prepareAsync()
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

        if (::rewardReceivedPlayer.isInitialized) {
            rewardReceivedPlayer.release()
        }
    }
}