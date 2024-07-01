package com.justcircleprod.btsquiz.quiz.presentation

import android.animation.LayoutTransition
import android.content.Intent
import android.content.res.ColorStateList
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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.button.MaterialButton
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.core.data.constants.CoinConstants
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.ImageQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion
import com.justcircleprod.btsquiz.core.presentation.animateProgress
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
import kotlinx.coroutines.Job
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
    private var isRightAnswerPlayerPlaying = false

    private lateinit var wrongAnswerPlayer: MediaPlayer
    private var isWrongAnswerPlayerPrepared = false
    private var isWrongAnswerPlayerPlaying = false

    private var audioQuestionPlayer: MediaPlayer? = null
    private var isAudioQuestionPlayerPlaying = false

    private var isVideoQuestionPlayerPlaying = false

    private lateinit var hint5050Player: MediaPlayer
    private var isHint5050PlayerPrepared = false
    private var isHint5050PlayerPlaying = false

    private var positionOfVideoPlayer = 0

    private var userCoinsQuantityCollectionJob: Job? = null
    private var questionWorthCollectionJob: Job? = null

    private var questionCollectionJob: Job? = null

    private var withoutQuizHintsCollectionJob: Job? = null

    private var hint5050UsedCollectionJob: Job? = null
    private var hintCorrectAnswerUsedCollectionJob: Job? = null

    companion object {
        const val LEVEL_ARGUMENT_NAME = "LEVEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)

        onBackPressedDispatcher.addCallback { startLevelsActivity() }

        enableAnimation()
        initAd()

        initRightAnswerPlayer()
        initWrongAnswerPlayer()

        setLoadingCollector()
        setOnOptionsClickListeners()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        refreshAdTimer?.start()
        resumePlayers()
    }

    override fun onPause() {
        super.onPause()

        refreshAdTimer?.cancel()
        pausePlayers()
    }

    private fun resumePlayers() {
        // VideoView becomes black after minimizing the application
        // if you don't set the position manually
        if (isVideoQuestionPlayerPlaying) {
            binding.videoQuestion.seekTo(positionOfVideoPlayer)
            binding.videoQuestion.start()
            // if VideoView has already finished playing, but is still on the screen
        } else if (binding.videoQuestion.visibility == View.VISIBLE) {
            binding.videoQuestion.seekTo(positionOfVideoPlayer)
        }

        if (isAudioQuestionPlayerPlaying) {
            audioQuestionPlayer?.start()
        }

        if (isRightAnswerPlayerPlaying) {
            rightAnswerPlayer.start()
        }

        if (isWrongAnswerPlayerPlaying) {
            wrongAnswerPlayer.start()
        }

        if (isHint5050PlayerPlaying) {
            hint5050Player.start()
        }
    }

    private fun pausePlayers() {
        if (isVideoQuestionPlayerPlaying) {
            binding.videoQuestion.pause()
            positionOfVideoPlayer = binding.videoQuestion.currentPosition
        }

        if (isAudioQuestionPlayerPlaying) {
            audioQuestionPlayer?.pause()
        }

        if (isRightAnswerPlayerPlaying) {
            rightAnswerPlayer.pause()
        }

        if (isWrongAnswerPlayerPlaying) {
            wrongAnswerPlayer.pause()
        }

        if (isHint5050PlayerPlaying) {
            hint5050Player.pause()
        }
    }

    private fun enableAnimation() {
        binding.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
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

                override fun onAdFailedToLoad(error: AdRequestError) {
                    destroyBannerAdView()
                }

                override fun onAdClicked() {}

                override fun onLeftApplication() {}

                override fun onReturnedToApplication() {}

                override fun onImpression(impressionData: ImpressionData?) {}
            })

            loadAd(AdRequest.Builder().build())
        }
    }

    private fun initRightAnswerPlayer() {
        rightAnswerPlayer = MediaPlayer()

        rightAnswerPlayer.setOnPreparedListener {
            isRightAnswerPlayerPrepared = true
            it.setOnPreparedListener(null)
        }

        rightAnswerPlayer.setOnCompletionListener {
            isRightAnswerPlayerPlaying = false
        }

        rightAnswerPlayer.setDataSource(
            this,
            Uri.parse("android.resource://$packageName/raw/right_answer")
        )
        rightAnswerPlayer.prepareAsync()
    }

    private fun initWrongAnswerPlayer() {
        wrongAnswerPlayer = MediaPlayer()

        wrongAnswerPlayer.setOnPreparedListener {
            isWrongAnswerPlayerPrepared = true
            it.setOnPreparedListener(null)
        }

        wrongAnswerPlayer.setOnCompletionListener {
            isWrongAnswerPlayerPlaying = false
        }

        wrongAnswerPlayer.setDataSource(
            this,
            Uri.parse("android.resource://$packageName/raw/wrong_answer")
        )
        wrongAnswerPlayer.prepareAsync()
    }

    private fun setLoadingCollector() {
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (viewModel.isFirstStart) {
                    viewModel.isFirstStart = false
                    return@collect
                }

                if (!isLoading) {
                    if (viewModel.questionsCount != 0) {
                        viewModel.setQuestionOnCurrentPosition()

                        setQuestionWorthCollectors()
                        setUserCoinsQuantityCollector()
                        setWithoutQuizHintsCollector()
                        setQuestionsCollector()

                        binding.videoQuestionLayout.clipToOutline = true

                        binding.quizProgress.max = viewModel.questionsCount * 100

                        binding.contentLayout.visibility = View.VISIBLE
                    } else {
                        // if the loading was successful, but there are no questions left for the user
                        setOutOfQuestionsLayout()
                    }
                }
            }
        }
    }

    private fun setOutOfQuestionsLayout() {
        binding.toLevelsBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.outOfQuestionsLayout.visibility = View.VISIBLE
    }

    private fun startLevelsActivity() {
        val intent = Intent(this, LevelsActivity::class.java)
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
        stopQuestionPlayers()

        if (viewModel.checkAnswer(btn.text.toString())) {
            onRightAnswer(btn)
        } else {
            onWrongAnswer(btn)
        }
    }

    private fun setQuestionWorthCollectors() {
        if (questionWorthCollectionJob != null) return

        questionWorthCollectionJob = lifecycleScope.launch {
            viewModel.questionWorth.collect {
                binding.questionWorth.text = getString(R.string.quiz_question_worth_label, it)
                binding.questionWorthLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setUserCoinsQuantityCollector() {
        if (userCoinsQuantityCollectionJob != null) return

        userCoinsQuantityCollectionJob = lifecycleScope.launch {
            viewModel.userCoinsQuantity.collect {
                if (it == null || !it.isDigitsOnly()) return@collect

                binding.userCoinsQuantity.text =
                    getString(R.string.quiz_users_coins_quantity, it.toInt())

                binding.userCoinsQuantityLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun setWithoutQuizHintsCollector() {
        if (withoutQuizHintsCollectionJob != null) return

        withoutQuizHintsCollectionJob = lifecycleScope.launch {
            viewModel.withoutQuizHints.collect {
                when (it) {
                    DataStoreConstants.WITHOUT_QUIZ_HINTS -> {
                        binding.hintDivider.visibility = View.GONE
                        binding.hint5050.visibility = View.GONE
                        binding.hintCorrectAnswer.visibility = View.GONE
                    }

                    DataStoreConstants.WITH_QUIZ_HINTS, null -> {
                        initHint5050Player()
                        setHintPrices()
                        setOnHintsClickListeners()
                        setHint5050UsedCollector()
                        setHintCorrectAnswerUsedCollector()

                        binding.hintDivider.visibility = View.VISIBLE
                        binding.hint5050.visibility = View.VISIBLE
                        binding.hintCorrectAnswer.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setQuestionsCollector() {
        if (questionCollectionJob != null) return

        questionCollectionJob = lifecycleScope.launch {
            viewModel.question.collect { question ->
                if (question == null) {
                    startResultActivity()
                    return@collect
                }

                hidePreviousQuestion()
                animateQuizProgress()
                setDefaultButtonColors()

                if (question is TextQuestion) {
                    setTextQuestionData(question)
                    enableButtons()
                } else {
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
    }

    private fun initHint5050Player() {
        if (isHint5050PlayerPrepared) return

        hint5050Player = MediaPlayer()

        hint5050Player.setOnPreparedListener {
            isHint5050PlayerPrepared = true
            it.setOnPreparedListener(null)
        }

        hint5050Player.setOnCompletionListener {
            isHint5050PlayerPlaying = false
        }

        hint5050Player.setDataSource(
            this,
            Uri.parse("android.resource://$packageName/raw/hint_50_50")
        )
        hint5050Player.prepareAsync()
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

                    if (isHint5050PlayerPrepared) {
                        hint5050Player.start()
                        isHint5050PlayerPlaying = true
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

    private fun setHint5050UsedCollector() {
        if (hint5050UsedCollectionJob != null) return

        hint5050UsedCollectionJob = lifecycleScope.launch {
            viewModel.hint5050Used.collect { hint5050Used ->
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
                        when {
                            option == binding.firstOption.text && binding.firstOption.isEnabled -> {
                                binding.firstOption.disableWithTransparency()
                            }

                            option == binding.secondOption.text && binding.secondOption.isEnabled -> {
                                binding.secondOption.disableWithTransparency()
                            }

                            option == binding.thirdOption.text && binding.thirdOption.isEnabled -> {
                                binding.thirdOption.disableWithTransparency()
                            }

                            option == binding.fourthOption.text && binding.fourthOption.isEnabled -> {
                                binding.fourthOption.disableWithTransparency()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setHintCorrectAnswerUsedCollector() {
        if (hintCorrectAnswerUsedCollectionJob != null) return

        hintCorrectAnswerUsedCollectionJob = lifecycleScope.launch {
            viewModel.hintCorrectAnswerUsed.collect { hintCorrectAnswerUsed ->
                if (hintCorrectAnswerUsed) {
                    viewModel.onRightAnswer()

                    binding.hint5050.disableWithTransparency()
                    binding.hintCorrectAnswer.disableWithTransparency()

                    disableButtons()
                    stopQuestionPlayers()

                    onRightAnswer(
                        btn = when (viewModel.rightAnswer) {
                            binding.firstOption.text -> binding.firstOption
                            binding.secondOption.text -> binding.secondOption
                            binding.thirdOption.text -> binding.thirdOption
                            binding.fourthOption.text -> binding.fourthOption
                            else -> return@collect
                        }
                    )
                }
            }
        }
    }

    private fun hidePreviousQuestion() {
        binding.textQuestion.visibility = View.INVISIBLE
        binding.imageQuestion.visibility = View.INVISIBLE
        binding.audioQuestion.visibility = View.INVISIBLE
        binding.videoQuestionLayout.visibility = View.INVISIBLE
    }

    private fun animateQuizProgress() {
        val currentProgress = (viewModel.questionPosition) * 100
        val newProgress = currentProgress + 100
        binding.quizProgress.animateProgress(currentProgress, newProgress)
    }

    private fun setDefaultButtonColors() {
        binding.firstOption.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.btn_background)
        )
        binding.secondOption.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.btn_background)
        )
        binding.thirdOption.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.btn_background)
        )
        binding.fourthOption.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.btn_background)
        )
    }

    private fun setTextQuestionData(question: TextQuestion) {
        binding.textQuestion.visibility = View.VISIBLE
        binding.textQuestion.text = question.question
    }

    private fun setImageQuestionData(question: ImageQuestion) {
        Glide
            .with(this)
            .load(
                Uri.parse("android.resource://$packageName/drawable/${question.imageEntryName}")
            )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.image_question_rounded_corner_size)))
            .into(binding.imageQuestion)

        binding.imageQuestion.visibility = View.VISIBLE
        enableButtons()
    }

    private fun setVideoQuestionData(question: VideoQuestion) {
        binding.videoQuestion.setOnPreparedListener {
            it.setOnInfoListener { _, info, _ ->
                if (info == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    isVideoQuestionPlayerPlaying = true
                    binding.videoQuestionLayout.visibility = View.VISIBLE
                    enableButtons()

                    return@setOnInfoListener true
                }
                return@setOnInfoListener false
            }
        }

        binding.videoQuestion.setOnCompletionListener {
            isVideoQuestionPlayerPlaying = false
            positionOfVideoPlayer = binding.videoQuestion.duration
        }

        binding.videoQuestion.setVideoURI(
            Uri.parse("android.resource://$packageName/raw/${question.videoEntryName}")
        )
        binding.videoQuestion.start()
    }

    private fun setAudioQuestionData(question: AudioQuestion) {
        if (audioQuestionPlayer == null) {
            audioQuestionPlayer = MediaPlayer()

            audioQuestionPlayer?.setOnPreparedListener {
                binding.audioQuestion.visibility = View.VISIBLE

                it?.start()
                isAudioQuestionPlayerPlaying = true

                enableButtons()
            }

            audioQuestionPlayer?.setOnCompletionListener {
                isAudioQuestionPlayerPlaying = false
            }
        }

        audioQuestionPlayer?.setDataSource(
            this,
            Uri.parse("android.resource://$packageName/raw/${question.audioEntryName}")
        )
        audioQuestionPlayer?.prepareAsync()
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

    private fun stopQuestionPlayers() {
        when {
            binding.videoQuestionLayout.visibility == View.VISIBLE -> {
                binding.videoQuestion.pause()
                isVideoQuestionPlayerPlaying = false
                positionOfVideoPlayer = binding.videoQuestion.currentPosition
            }

            binding.audioQuestion.visibility == View.VISIBLE -> {
                audioQuestionPlayer?.pause()
                isAudioQuestionPlayerPlaying = false
            }
        }
    }

    private fun onRightAnswer(btn: MaterialButton) {
        if (isRightAnswerPlayerPrepared) {
            rightAnswerPlayer.start()
            isRightAnswerPlayerPlaying = true
        }

        btn.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.correct_answer_color)
        )

        val timer = object : CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                resetQuestionPlayers()
                viewModel.setQuestionOnNextPosition()
            }
        }
        timer.start()
    }

    private fun onWrongAnswer(btn: MaterialButton) {
        if (isWrongAnswerPlayerPrepared) {
            wrongAnswerPlayer.start()
            isWrongAnswerPlayerPlaying = true
        }

        btn.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.incorrect_answer_color)
        )

        var isRightAnswerShown = false

        val timer = object : CountDownTimer(2000, 250) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isRightAnswerShown && millisUntilFinished <= 1750) {
                    showRightAnswer(viewModel.rightAnswer)
                    isRightAnswerShown = true
                }
            }

            override fun onFinish() {
                resetQuestionPlayers()
                viewModel.setQuestionOnNextPosition()
            }
        }
        timer.start()
    }

    private fun showRightAnswer(rightAnswer: String) {
        when (rightAnswer) {
            binding.firstOption.text ->
                binding.firstOption.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.correct_answer_color)
                )

            binding.secondOption.text ->
                binding.secondOption.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.correct_answer_color)
                )

            binding.thirdOption.text ->
                binding.thirdOption.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.correct_answer_color)
                )

            binding.fourthOption.text ->
                binding.fourthOption.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.correct_answer_color)
                )
        }
    }

    private fun resetQuestionPlayers() {
        when {
            binding.videoQuestionLayout.visibility == View.VISIBLE -> {
                binding.videoQuestion.stopPlayback()
                positionOfVideoPlayer = 0
                binding.videoQuestion.setOnPreparedListener(null)
                binding.videoQuestion.setOnCompletionListener(null)
            }

            binding.audioQuestion.visibility == View.VISIBLE -> {
                audioQuestionPlayer?.stop()
                audioQuestionPlayer?.reset()
            }
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

    private fun destroyBannerAdView() {
        refreshAdTimer?.cancel()
        refreshAdTimer = null

        bannerAdView?.destroy()
        bannerAdView = null
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyBannerAdView()

        if (isRightAnswerPlayerPrepared) {
            rightAnswerPlayer.release()
        }

        if (isWrongAnswerPlayerPrepared) {
            wrongAnswerPlayer.release()
        }

        audioQuestionPlayer?.release()

        if (isHint5050PlayerPrepared) {
            hint5050Player.release()
        }
    }
}