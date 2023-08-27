package com.justcircleprod.btsquiz.ui.quizResult.doubleCoinsConfirmationDialog

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
import com.justcircleprod.btsquiz.databinding.DialogDoubleCoinsConfirmationBinding
import com.justcircleprod.btsquiz.ui.common.RewardedAdState
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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

    private lateinit var rewardedAd: RewardedAd
    private var rewardedAdLoaded = false

    private lateinit var rewardReceivedPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        earnedCoins = arguments?.getInt(EARNED_COINS_NAME_ARGUMENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext(), R.style.DialogRoundedCorner)

        binding = DialogDoubleCoinsConfirmationBinding.inflate(layoutInflater)

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

        binding.submitRewardResultBtn.setOnClickListener {
            if (viewModel.rewardedAdState.value == RewardedAdState.RewardReceived) {
                (activity as DoubleCoinsConfirmationDialogCallback).onSubmitReward()
            }
            dismiss()
        }
    }

    private fun loadRewardedAd() {
        rewardedAd = RewardedAd(requireContext())

        val adUnitId =
            String(Base64.decode("Ui1NLTI0NjM5MTktNA==", Base64.DEFAULT), Charsets.UTF_8)
        rewardedAd.setAdUnitId(adUnitId)

        val adRequest = AdRequest.Builder().build()

        rewardedAd.setRewardedAdEventListener(object : RewardedAdEventListener {
            override fun onAdLoaded() {
                rewardedAdLoaded = true
                rewardedAd.show()
            }

            override fun onAdFailedToLoad(p0: AdRequestError) {
                viewModel.rewardedAdState.value = RewardedAdState.FailedToLoad
                rewardedAd.destroy()
            }

            override fun onAdShown() {}

            override fun onAdDismissed() {
                if (viewModel.rewardReceived) {
                    viewModel.rewardedAdState.value = RewardedAdState.RewardReceived
                }

                rewardedAd.destroy()
            }

            override fun onRewarded(p0: Reward) {
                viewModel.doubleEarnedCoins()
                viewModel.rewardReceived = true
            }

            override fun onAdClicked() {}

            override fun onLeftApplication() {}

            override fun onReturnedToApplication() {}

            override fun onImpression(p0: ImpressionData?) {}
        })

        rewardedAd.loadAd(adRequest)

        // to limit the time to load an ad
        object : CountDownTimer(7000, 7000) {
            override fun onTick(mills: Long) {}

            override fun onFinish() {
                if (!rewardedAdLoaded) {
                    viewModel.rewardedAdState.value = RewardedAdState.FailedToLoad
                    rewardedAd.destroy()
                }
            }
        }.start()
    }

    private fun setStateObserver() {
        viewModel.rewardedAdState.observe(this) {
            binding.questionLayout.visibility = View.GONE
            binding.loadingLayout.visibility = View.GONE
            binding.rewardResultLayout.visibility = View.GONE
            binding.rewardResultQuantityLayout.visibility = View.GONE

            when (it) {
                RewardedAdState.UserNotAgreedYet -> {
                    binding.title.text = getString(R.string.double_coins)

                    binding.questionLayout.visibility = View.VISIBLE

                    if (earnedCoins != null) {
                        binding.earnedCoinsToDouble.text = earnedCoins.toString()
                    }
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
                    binding.title.text = getString(R.string.coins_doubled_title)

                    binding.rewardResultTv.text = getString(R.string.coins_doubled)
                    if (earnedCoins != null) {
                        binding.rewardResultQuantity.text = (earnedCoins!! * 2).toString()
                    }

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

    override fun onDestroy() {
        super.onDestroy()

        if (::rewardReceivedPlayer.isInitialized) {
            rewardReceivedPlayer.release()
        }
    }
}