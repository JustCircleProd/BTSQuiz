package com.justcircleprod.btsquiz.quiz.presentation

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.ImageQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion
import com.justcircleprod.btsquiz.core.presentation.disableWithTransparency
import com.justcircleprod.btsquiz.core.presentation.enable
import com.justcircleprod.btsquiz.databinding.ActivityQuizBinding
import com.justcircleprod.btsquiz.levels.presentation.LevelsActivity
import com.justcircleprod.btsquiz.quizResult.presentation.QuizResultActivity
import com.justcircleprod.btsquiz.watchRewardedAd.presentation.WatchRewardedAdConfirmationDialog
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@AndroidEntryPoint
class QuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()

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

    private lateinit var rightAnswerPlayer: MediaPlayer
    private var isRightAnswerPlayerPrepared = false

    private lateinit var wrongAnswerPlayer: MediaPlayer
    private var isWrongAnswerPlayerPrepared = false

    private var musicPlayer: MediaPlayer? = null

    private lateinit var hint5050Player: MediaPlayer
    private var hint5050PlayerPrepared = false

    private var positionOfVideoPlayer = 0

    companion object {
        const val LEVEL_ARGUMENT_NAME = "LEVEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)

        onBackPressedDispatcher.addCallback { startLevelsActivity() }

        enableAnimation()
        setLoadingGif()

        initAd()
        initAnswerPlayers()

        setLoadingObserver()

        setOnOptionsClickListeners()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        resumePlayers()
        refreshAdTimer?.start()
    }

    override fun onPause() {
        super.onPause()
        pausePlayers()
        refreshAdTimer?.cancel()
    }

    private fun resumePlayers() {
        when {
            binding.videoQuestionLayout.visibility == View.VISIBLE -> {
                binding.videoQuestion.seekTo(positionOfVideoPlayer)
                binding.videoQuestion.start()
            }
            // if the application was minimized when downloading the video
            viewModel.question.value is VideoQuestion &&
                    binding.videoQuestionLayout.visibility == View.INVISIBLE -> {
                setVideoQuestionData(viewModel.question.value!! as VideoQuestion)
            }

            musicPlayer != null && binding.audioQuestion.visibility == View.VISIBLE -> {
                musicPlayer?.start()
            }
            // if the application was minimized when downloading the audio
            viewModel.question.value is AudioQuestion &&
                    binding.audioQuestion.visibility == View.INVISIBLE -> {
                setAudioQuestionData(viewModel.question.value!! as AudioQuestion)
            }
        }
    }

    private fun pausePlayers() {
        when {
            binding.videoQuestionLayout.visibility == View.VISIBLE -> {
                binding.videoQuestion.pause()
                positionOfVideoPlayer = binding.videoQuestion.currentPosition
            }
            // if the application was minimized when downloading the audio
            viewModel.question.value is AudioQuestion &&
                    binding.audioQuestion.visibility == View.INVISIBLE -> {
                musicPlayer = null
            }

            musicPlayer != null && binding.audioQuestion.visibility == View.VISIBLE -> {
                musicPlayer?.pause()
            }
        }
    }

    private fun enableAnimation() {
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

    private fun initAd() {
        binding.bannerAdView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.bannerAdView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                binding.bannerAdView.apply {
                    setAdSize(this@QuizActivity.adSize)

                    val adUnitId =
                        String(
                            Base64.decode("Ui1NLTI0NjM5MTktMQ==", Base64.DEFAULT),
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

                override fun onAdFailedToLoad(error: AdRequestError) {}

                override fun onAdClicked() {}

                override fun onLeftApplication() {}

                override fun onReturnedToApplication() {}

                override fun onImpression(impressionData: ImpressionData?) {}
            })

            loadAd(AdRequest.Builder().build())
        }
    }

    private fun initAnswerPlayers() {
        rightAnswerPlayer = MediaPlayer()

        rightAnswerPlayer.setOnPreparedListener {
            isRightAnswerPlayerPrepared = true
        }

        lifecycleScope.launch(Dispatchers.IO) {
            rightAnswerPlayer.setDataSource(
                this@QuizActivity,
                Uri.parse("android.resource://$packageName/raw/correct_answer")
            )
            rightAnswerPlayer.prepareAsync()
        }

        wrongAnswerPlayer = MediaPlayer()

        wrongAnswerPlayer.setOnPreparedListener {
            isWrongAnswerPlayerPrepared = true
        }

        lifecycleScope.launch(Dispatchers.IO) {
            wrongAnswerPlayer.setDataSource(
                this@QuizActivity,
                Uri.parse("android.resource://$packageName/raw/incorrect_answer")
            )
            wrongAnswerPlayer.prepareAsync()
        }
    }

    private fun setLoadingObserver() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (viewModel.isFirstStart) {
                viewModel.isFirstStart = false
                return@observe
            }

            if (!isLoading) {
                if (viewModel.questionsCount != 0) {
                    viewModel.setQuestionOnCurrentPosition()

                    setCoinsObservers()
                    setWithoutQuizHintsCollector()
                    setQuestionsObserver()

                    binding.progress.max = viewModel.questionsCount

                    binding.loadLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                } else {
                    // if the download was successful, but there are no questions left for the user
                    setOutOfQuestionsLayout()
                }
            }
        }
    }

    private fun setOutOfQuestionsLayout() {
        binding.loadLayout.visibility = View.GONE

        binding.toLevelsBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.outOfQuestionsLayout.visibility = View.VISIBLE
    }

    private fun startLevelsActivity() {
        val intent = Intent(this@QuizActivity, LevelsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setOnOptionsClickListeners() {
        binding.firstOption.setOnClickListener { onOptionClicked(it as MaterialButton) }
        binding.secondOption.setOnClickListener { onOptionClicked(it as MaterialButton) }
        binding.thirdOption.setOnClickListener { onOptionClicked(it as MaterialButton) }
        binding.fourthOption.setOnClickListener { onOptionClicked(it as MaterialButton) }
    }

    private fun onOptionClicked(btn: MaterialButton) {
        disableButtons()
        stopAndResetPlayers()

        if (viewModel.checkAnswer(btn.text.toString())) {
            onRightAnswer(btn)
        } else {
            onWrongAnswer(btn)
        }
    }

    private fun setCoinsObservers() {
        viewModel.questionWorth.observe(this) {
            binding.questionWorth.text = getString(R.string.quiz_question_worth_label, it)
        }

        lifecycleScope.launch {
            viewModel.userCoinsQuantity.collect {
                val userCoinsQuantity = it?.toInt() ?: 0

                binding.userCoinsTv.text =
                    getString(R.string.quiz_users_coins_quantity, userCoinsQuantity)
            }
        }
    }

    private fun setWithoutQuizHintsCollector() {
        lifecycleScope.launch {
            viewModel.withoutQuizHintsState.collect {
                if (it == DataStoreConstants.WITHOUT_QUIZ_HINTS) {
                    binding.hintDivider.visibility = View.GONE
                    binding.hint5050.visibility = View.GONE
                    binding.hintCorrectAnswer.visibility = View.GONE
                } else {
                    prepareHint5050Player()
                    setHintPrices()
                    setOnHintsClickListeners()
                    setHint5050Observer()
                    setHintCorrectAnswerObserver()

                    binding.hintDivider.visibility = View.VISIBLE
                    binding.hint5050.visibility = View.VISIBLE
                    binding.hintCorrectAnswer.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setQuestionsObserver() {
        if (viewModel.question.hasActiveObservers()) return

        viewModel.question.observe(this) { question ->
            if (question == null) {
                startResultActivity()
                return@observe
            }

            hidePreviousQuestion()
            updateViews()

            if (question is TextQuestion) {
                setTextQuestionData(question)
                enableButtons()
            } else {
                binding.contentLoadingProgress.visibility = View.VISIBLE

                when (question) {
                    is ImageQuestion -> {
                        setImageQuestionData(question)
                    }

                    is VideoQuestion -> {
                        setVideoQuestionData(question)
                    }

                    is AudioQuestion -> {
                        setAudioQuestionData(question)
                    }
                }
            }

            binding.firstOption.text = question.firstOption
            binding.secondOption.text = question.secondOption
            binding.thirdOption.text = question.thirdOption
            binding.fourthOption.text = question.fourthOption
        }
    }

    private fun prepareHint5050Player() {
        if (hint5050PlayerPrepared) return

        hint5050Player = MediaPlayer()

        hint5050Player.setOnPreparedListener {
            hint5050PlayerPrepared = true
        }

        lifecycleScope.launch(Dispatchers.IO) {
            hint5050Player.setDataSource(
                this@QuizActivity,
                Uri.parse("android.resource://$packageName/raw/hint_50_50")
            )
            hint5050Player.prepareAsync()
        }
    }

    private fun setHintPrices() {
        binding.hint5050Price.text = CoinConstants.HINT_50_50_PRICE.toString()
        binding.hintCorrectAnswerPrice.text = CoinConstants.HINT_CORRECT_ANSWER_PRICE.toString()
    }

    private fun setOnHintsClickListeners() {
        binding.hint5050.setOnClickListener {
            lifecycleScope.launch {
                val userCoinsQuantity =
                    viewModel.userCoinsQuantity.first()?.toInt() ?: return@launch

                if (userCoinsQuantity >= CoinConstants.HINT_50_50_PRICE) {
                    viewModel.useHint5050()
                    if (hint5050PlayerPrepared) {
                        hint5050Player.start()
                    }
                } else {
                    WatchRewardedAdConfirmationDialog.newInstance(CoinConstants.HINT_50_50_PRICE - userCoinsQuantity)
                        .show(supportFragmentManager, null)
                }
            }
        }

        binding.hintCorrectAnswer.setOnClickListener {
            lifecycleScope.launch {
                val userCoinsQuantity =
                    viewModel.userCoinsQuantity.first()?.toInt() ?: return@launch

                if (userCoinsQuantity >= CoinConstants.HINT_CORRECT_ANSWER_PRICE) {
                    viewModel.useHintCorrectAnswer()
                } else {
                    WatchRewardedAdConfirmationDialog.newInstance(CoinConstants.HINT_CORRECT_ANSWER_PRICE - userCoinsQuantity)
                        .show(supportFragmentManager, null)
                }
            }
        }
    }

    private fun setHint5050Observer() {
        if (viewModel.hint5050Used.hasActiveObservers()) return

        viewModel.hint5050Used.observe(this) { hint5050Used ->
            if (hint5050Used) {
                binding.hint5050.disableWithTransparency()
                binding.hintCorrectAnswer.disableWithTransparency()

                var options = mutableListOf(
                    binding.firstOption.text,
                    binding.secondOption.text,
                    binding.thirdOption.text,
                    binding.fourthOption.text,
                )
                options.remove(viewModel.rightAnswer)
                options = options.shuffled().toMutableList().take(2).toMutableList()

                options.forEach { option ->
                    when (option) {
                        binding.firstOption.text -> {
                            binding.firstOption.disableWithTransparency()
                        }

                        binding.secondOption.text -> {
                            binding.secondOption.disableWithTransparency()
                        }

                        binding.thirdOption.text -> {
                            binding.thirdOption.disableWithTransparency()
                        }

                        binding.fourthOption.text -> {
                            binding.fourthOption.disableWithTransparency()
                        }
                    }
                }
            }
        }
    }

    private fun setHintCorrectAnswerObserver() {
        if (viewModel.hintCorrectAnswerUsed.hasActiveObservers()) return

        viewModel.hintCorrectAnswerUsed.observe(this) { hintCorrectAnswerUsed ->
            if (hintCorrectAnswerUsed) {
                viewModel.onRightAnswer()

                binding.hint5050.disableWithTransparency()
                binding.hintCorrectAnswer.disableWithTransparency()

                disableButtons()
                stopAndResetPlayers()

                onRightAnswer(
                    when (viewModel.rightAnswer) {
                        binding.firstOption.text -> binding.firstOption
                        binding.secondOption.text -> binding.secondOption
                        binding.thirdOption.text -> binding.thirdOption
                        binding.fourthOption.text -> binding.fourthOption
                        else -> return@observe
                    }
                )
            }
        }
    }

    private fun hidePreviousQuestion() {
        binding.textQuestion.visibility = View.INVISIBLE
        binding.imageQuestion.visibility = View.INVISIBLE
        binding.audioQuestion.visibility = View.INVISIBLE
        binding.videoQuestionLayout.visibility = View.INVISIBLE
    }

    private fun updateViews() {
        binding.progress.progress++

        binding.firstOption.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_background))
        binding.secondOption.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.btn_background
            )
        )
        binding.thirdOption.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_background))
        binding.fourthOption.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.btn_background
            )
        )
    }

    private fun setTextQuestionData(question: TextQuestion) {
        binding.textQuestion.visibility = View.VISIBLE
        binding.textQuestion.text = question.question
    }

    private fun setImageQuestionData(question: ImageQuestion) {
        binding.imageQuestion.visibility = View.INVISIBLE

        Glide
            .with(this)
            .load(
                resources.getIdentifier(
                    question.imageEntryName,
                    "drawable",
                    packageName
                )
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imageQuestion)

        binding.contentLoadingProgress.visibility = View.GONE
        binding.imageQuestion.visibility = View.VISIBLE
        enableButtons()
    }

    private fun setVideoQuestionData(question: VideoQuestion) {
        binding.videoQuestion.setOnPreparedListener {
            it.setOnInfoListener { _, info, _ ->
                if (info == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    binding.contentLoadingProgress.visibility = View.GONE
                    binding.videoQuestionLayout.visibility = View.VISIBLE
                    enableButtons()

                    return@setOnInfoListener true
                }
                return@setOnInfoListener false
            }
        }

        binding.videoQuestion.setVideoURI(
            Uri.parse("android.resource://$packageName/raw/${question.videoEntryName}")
        )
        binding.videoQuestion.start()

    }

    private fun setAudioQuestionData(question: AudioQuestion) {
        musicPlayer = MediaPlayer()

        musicPlayer?.setOnPreparedListener {
            if (musicPlayer != null) {
                binding.contentLoadingProgress.visibility = View.GONE
                binding.audioQuestion.visibility = View.VISIBLE
                musicPlayer!!.start()
                enableButtons()
            }
        }

        musicPlayer?.setDataSource(
            this,
            Uri.parse("android.resource://$packageName/raw/${question.audioEntryName}")
        )
        musicPlayer?.prepareAsync()
    }

    private fun enableButtons() {
        binding.firstOption.enable()
        binding.secondOption.enable()
        binding.thirdOption.enable()
        binding.fourthOption.enable()

        binding.hint5050.enable()
        binding.hintCorrectAnswer.enable()
    }

    private fun disableButtons() {
        binding.firstOption.isEnabled = false
        binding.secondOption.isEnabled = false
        binding.thirdOption.isEnabled = false
        binding.fourthOption.isEnabled = false

        binding.hint5050.disableWithTransparency()
        binding.hintCorrectAnswer.disableWithTransparency()
    }

    private fun stopAndResetPlayers() {
        when {
            binding.videoQuestionLayout.visibility == View.VISIBLE -> {
                binding.videoQuestion.stopPlayback()
                positionOfVideoPlayer = 0
                binding.videoQuestion.setOnPreparedListener(null)
            }

            binding.audioQuestion.visibility == View.VISIBLE -> {
                musicPlayer?.stop()
                musicPlayer?.release()
                musicPlayer?.setOnPreparedListener(null)
                musicPlayer = null
            }
        }
    }

    private fun onRightAnswer(btn: MaterialButton) {
        if (isRightAnswerPlayerPrepared) {
            rightAnswerPlayer.start()
        }

        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_answer_color))

        val timer = object : CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                viewModel.setQuestionOnNextPosition()
            }
        }
        timer.start()
    }

    private fun onWrongAnswer(btn: MaterialButton) {
        if (isWrongAnswerPlayerPrepared) {
            wrongAnswerPlayer.start()
        }

        btn.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_answer_color))

        var isRightAnswerShown = false

        val timer = object : CountDownTimer(2000, 250) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isRightAnswerShown && millisUntilFinished <= 1750) {
                    showRightAnswer(viewModel.rightAnswer)
                    isRightAnswerShown = true
                }
            }

            override fun onFinish() {
                viewModel.setQuestionOnNextPosition()
            }
        }
        timer.start()
    }

    private fun showRightAnswer(rightAnswer: String) {
        when (rightAnswer) {
            binding.firstOption.text ->
                binding.firstOption.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.correct_answer_color
                    )
                )

            binding.secondOption.text ->
                binding.secondOption.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.correct_answer_color
                    )
                )

            binding.thirdOption.text ->
                binding.thirdOption.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.correct_answer_color
                    )
                )

            binding.fourthOption.text ->
                binding.fourthOption.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.correct_answer_color
                    )
                )
        }
    }

    private fun startResultActivity() {
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra(QuizResultActivity.LEVEL_ARGUMENT_NAME, viewModel.levelId)
        intent.putExtra(QuizResultActivity.EARNED_COINS_ARGUMENT_NAME, viewModel.earnedCoins)
        intent.putExtra(
            QuizResultActivity.CORRECTLY_ANSWERED_QUESTIONS_COUNT_ARGUMENT_NAME,
            viewModel.correctlyAnsweredQuestionsCount
        )
        intent.putExtra(
            QuizResultActivity.QUESTIONS_COUNT_ARGUMENT_NAME,
            viewModel.questionsCount
        )
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        refreshAdTimer?.cancel()
        bannerAdView?.destroy()

        rightAnswerPlayer.release()
        wrongAnswerPlayer.release()

        musicPlayer?.release()

        if (hint5050PlayerPrepared) {
            hint5050Player.release()
        }
    }
}