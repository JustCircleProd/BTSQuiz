package com.justcircleprod.btsquiz.ui.quiz

import android.animation.LayoutTransition
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Base64
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.data.models.AudioQuestion
import com.justcircleprod.btsquiz.data.models.ImageQuestion
import com.justcircleprod.btsquiz.data.models.TextQuestion
import com.justcircleprod.btsquiz.data.models.VideoQuestion
import com.justcircleprod.btsquiz.databinding.ActivityQuizBinding
import com.justcircleprod.btsquiz.ui.quizResult.QuizResultActivity
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuizActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()

    private lateinit var rightAnswerPlayer: MediaPlayer
    private var isRightAnswerPlayerPrepared = false

    private lateinit var wrongAnswerPlayer: MediaPlayer
    private var isWrongAnswerPlayerPrepared = false

    private var musicPlayer: MediaPlayer? = null

    private var positionOfVideoPlayer = 0

    companion object {
        const val CATEGORY_ID_ARGUMENT_NAME = "categoryId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)

        enableAnimation()
        initAd()
        initAnswerPlayers()
        setOnOptionsClickListeners()
        setLoadingObserver()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        resumePlayers()
    }

    override fun onPause() {
        super.onPause()
        pausePlayers()
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

    override fun onClick(view: View?) {
        if (view is MaterialButton) {
            disableButtons()
            stopAndResetPlayers()
            if (viewModel.checkAnswer(view.text.toString())) {
                onRightAnswer(view)
            } else {
                onWrongAnswer(view)
            }
        }
    }

    private fun enableAnimation() {
        binding.contentLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
    }

    private fun initAd() {
        val adUnitId =
            String(Base64.decode("Ui1NLTI0NjM5MTktMQ==", Base64.DEFAULT), Charsets.UTF_8)

        binding.bannerAdView.setAdUnitId(adUnitId)
        binding.bannerAdView.setAdSize(AdSize.stickySize(this@QuizActivity, 320))

        val adRequest = AdRequest.Builder().build()
        binding.bannerAdView.setBannerAdEventListener(object : BannerAdEventListener {
            override fun onAdLoaded() {
                binding.bannerAdView.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(p0: AdRequestError) {}

            override fun onAdClicked() {}

            override fun onLeftApplication() {}

            override fun onReturnedToApplication() {}

            override fun onImpression(p0: ImpressionData?) {}
        })

        binding.bannerAdView.loadAd(adRequest)
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

    private fun setOnOptionsClickListeners() {
        binding.firstOption.setOnClickListener(this)
        binding.secondOption.setOnClickListener(this)
        binding.thirdOption.setOnClickListener(this)
        binding.fourthOption.setOnClickListener(this)
    }

    private fun setLoadingObserver() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (viewModel.isFirstStart) {
                viewModel.isFirstStart = false
                return@observe
            }

            if (!isLoading) {
                if (viewModel.questions.size == viewModel.countOfQuestions) {
                    viewModel.setQuestionOnCurrentPosition()

                    setScoreObserver()
                    setQuestionsObserver()

                    binding.loadLayout.visibility = View.GONE
                    binding.contentLayout.visibility = View.VISIBLE
                } else {
                    // if the download was successful, but because of the "no repeat" option,
                    // there are no questions left for the user
                    binding.loadLayout.visibility = View.GONE
                    setOutOfQuestionsLayout()
                }
            }

        }
    }

    private fun setOutOfQuestionsLayout() {
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.outOfQuestionsLayout.visibility = View.VISIBLE
    }

    private fun setScoreObserver() {
        viewModel.score.observe(this) {
            binding.score.text = getString(R.string.tv_score_label, it)
        }
    }

    private fun setQuestionsObserver() {
        viewModel.question.observe(this) { question ->
            if (question == null) {
                startResultActivity()
                return@observe
            }

            hidePreviousQuestion()
            updateViews()

            binding.points.text = getString(R.string.tv_points_label, question.points)

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

    private fun hidePreviousQuestion() {
        binding.textQuestion.visibility = View.INVISIBLE
        binding.imageQuestion.visibility = View.INVISIBLE
        binding.audioQuestion.visibility = View.INVISIBLE
        binding.videoQuestionLayout.visibility = View.INVISIBLE
    }

    private fun updateViews() {
        binding.progress.progress++

        binding.firstOption.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.secondOption.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.thirdOption.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.fourthOption.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
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
                    question.image_entry_name,
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
            Uri.parse("android.resource://$packageName/raw/${question.video_entry_name}")
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
            Uri.parse("android.resource://$packageName/raw/${question.audio_entry_name}")
        )
        musicPlayer?.prepareAsync()
    }

    private fun enableButtons() {
        binding.firstOption.isEnabled = true
        binding.secondOption.isEnabled = true
        binding.thirdOption.isEnabled = true
        binding.fourthOption.isEnabled = true
    }

    private fun disableButtons() {
        binding.firstOption.isEnabled = false
        binding.secondOption.isEnabled = false
        binding.thirdOption.isEnabled = false
        binding.fourthOption.isEnabled = false
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

        val timer = object : CountDownTimer(2000, 250) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <= 1750) {
                    showRightAnswer(viewModel.question.value!!.answerNum)
                }
            }

            override fun onFinish() {
                viewModel.setQuestionOnNextPosition()
            }
        }
        timer.start()
    }

    private fun showRightAnswer(answerNum: Int) {
        when (answerNum) {
            1 ->
                binding.firstOption.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.correct_answer_color
                    )
                )
            2 ->
                binding.secondOption.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.correct_answer_color
                    )
                )
            3 ->
                binding.thirdOption.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.correct_answer_color
                    )
                )
            4 ->
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
        intent.putExtra(QuizResultActivity.CATEGORY_ID_ARGUMENT_NAME, viewModel.categoryId)
        intent.putExtra(QuizResultActivity.SCORE_ARGUMENT_NAME, viewModel.score.value)
        startActivity(intent)
        finish()
    }

    /*override fun onDestroy() {
        super.onDestroy()
        binding.bannerAdView.destroy()
    }*/
}