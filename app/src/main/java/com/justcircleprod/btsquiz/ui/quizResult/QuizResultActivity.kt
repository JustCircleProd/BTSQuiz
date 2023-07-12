package com.justcircleprod.btsquiz.ui.quizResult

import android.animation.LayoutTransition
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.justcircleprod.btsquiz.App
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.ActivityQuizResultBinding
import com.justcircleprod.btsquiz.ui.quiz.QuizActivity
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.interstitial.InterstitialAd
import com.yandex.mobile.ads.interstitial.InterstitialAdEventListener

@AndroidEntryPoint
class QuizResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizResultBinding
    private val viewModel: QuizResultViewModel by viewModels()

    private lateinit var interstitialAd: InterstitialAd
    private var isAdShown = false

    private lateinit var resultPlayer: MediaPlayer

    companion object {
        const val CATEGORY_ID_ARGUMENT_NAME = "categoryId"
        const val SCORE_ARGUMENT_NAME = "score"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)

        enableAnimations()

        setLoadingObserver()
        workWithInterstitialAd()
        setScoresObservers()

        setOnShareResultBtnClickListener()

        setOnRepeatQuizBtnClickListener()
        binding.toCategoriesBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        setContentView(binding.root)
    }

    private fun enableAnimations() {
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun setLoadingObserver() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading.all { !it }) {
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

        interstitialAd = InterstitialAd(this)

        val adUnitId =
            String(Base64.decode("Ui1NLTI0NjM5MTktMg==", Base64.DEFAULT), Charsets.UTF_8)

        interstitialAd.setAdUnitId(adUnitId)

        val adRequest = AdRequest.Builder().build()

        interstitialAd.setInterstitialAdEventListener(object : InterstitialAdEventListener {
            override fun onAdLoaded() {
                interstitialAd.show()
            }

            override fun onAdFailedToLoad(p0: AdRequestError) {
                viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
                interstitialAd.destroy()
            }

            override fun onAdShown() {
                isAdShown = true
            }

            override fun onAdDismissed() {
                viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
                interstitialAd.destroy()
            }

            override fun onAdClicked() {}

            override fun onLeftApplication() {}

            override fun onReturnedToApplication() {}

            override fun onImpression(p0: ImpressionData?) {}
        })

        interstitialAd.loadAd(adRequest)

        // to limit the time to load an ad
        object : CountDownTimer(3500, 1000) {
            override fun onTick(mills: Long) {}

            override fun onFinish() {
                if (!isAdShown) {
                    viewModel.isLoading.value = listOf(viewModel.isLoading.value!![0], false)
                    interstitialAd.destroy()
                }
            }
        }.start()
    }

    private fun setScoresObservers() {
        viewModel.currentScore.observe(this) {
            binding.score.text = it.toString()
        }
        viewModel.lastScore.observe(this) {
            binding.bestScore.text = it.toString()
        }
    }

    private fun showResult() {
        when {
            viewModel.currentScore.value!! > viewModel.lastScore.value!! -> {
                binding.bestScoreLabel.visibility = View.GONE
                binding.bestScore.visibility = View.GONE

                val celebrationText =
                    resources.getStringArray(R.array.texts_for_best_result).toList().shuffled()[0]
                binding.resultText.text =
                    getString(R.string.new_record_with_celebration_text, celebrationText)

                val imageResource = listOf(
                    R.drawable.best_result,
                    R.drawable.best_result_2,
                    R.drawable.best_result_3
                ).shuffled()[0]

                Glide
                    .with(this)
                    .load(imageResource)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.resultImage)

                playResultSound("best_result")
            }
            viewModel.currentScore.value!! < 1000 -> {
                binding.resultText.text =
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
                    .into(binding.resultImage)

                playResultSound("bad_result")
            }
            else -> {
                binding.resultText.text =
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
                    .into(binding.resultImage)

                playResultSound("good_result")
            }
        }
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

    private fun setOnShareResultBtnClickListener() {
        binding.shareResultBtn.setOnClickListener {
            if (viewModel.currentScore.value == null) {
                Toast.makeText(
                    this,
                    getString(R.string.sharing_result_not_available),
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val resultStr =
                getString(
                    R.string.for_sharing_result,
                    getCategoryName(),
                    viewModel.currentScore.value!!
                )

            val playStoreLink = getString(R.string.play_store_link)

            val shareContentBuilder = StringBuilder()
            shareContentBuilder.append(resultStr, '\n', playStoreLink)

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    shareContentBuilder.toString()
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun getCategoryName(): String {
        return when (viewModel.categoryId) {
            1 -> {
                getString(R.string.random_questions)
            }
            2 -> {
                getString(R.string.text_questions)
            }
            3 -> {
                getString(R.string.image_questions)
            }
            4 -> {
                getString(R.string.video_questions)
            }
            5 -> {
                getString(R.string.audio_questions)
            }
            else -> {
                getString(R.string.random_questions)
            }
        }
    }

    private fun setOnRepeatQuizBtnClickListener() {
        binding.repeatQuizBtn.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(CATEGORY_ID_ARGUMENT_NAME, this.intent.extras!!.getInt(CATEGORY_ID_ARGUMENT_NAME))
            startActivity(intent)
            finish()
        }
    }
}