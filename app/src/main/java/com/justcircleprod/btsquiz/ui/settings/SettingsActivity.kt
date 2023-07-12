package com.justcircleprod.btsquiz.ui.settings

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.data.room.constants.DifficultyState
import com.justcircleprod.btsquiz.data.room.constants.QuestionsRepetitionState
import com.justcircleprod.btsquiz.databinding.ActivitySettingsBinding
import com.justcircleprod.btsquiz.ui.settings.developersAndLicenses.DevelopersAndLicensesActivity

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        setDifficultyObserver()
        setOnDifficultyChipsClickListeners()

        setQuestionsRepetitionObserver()
        makeResetPassedQuestionsClickable()

        makeDevelopersAndLicensesTextClickable()

        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        setContentView(binding.root)
    }

    private fun setDifficultyObserver() {
        viewModel.difficultyState.observe(this) { state ->
            when (state) {
                DifficultyState.RANDOM -> {
                    binding.chipRandom.isChecked = true
                }
                DifficultyState.USUAL -> {
                    binding.chipUsual.isChecked = true
                }
                DifficultyState.DIFFICULT -> {
                    binding.chipDifficult.isChecked = true
                }
                else -> {
                    binding.chipRandom.isChecked = true
                }
            }
        }
    }

    private fun setOnDifficultyChipsClickListeners() {
        binding.chipRandom.setOnClickListener {
            viewModel.updateDifficulty(
                this,
                DifficultyState.RANDOM
            )
        }
        binding.chipUsual.setOnClickListener {
            viewModel.updateDifficulty(
                this,
                DifficultyState.USUAL
            )
        }
        binding.chipDifficult.setOnClickListener {
            viewModel.updateDifficulty(
                this,
                DifficultyState.DIFFICULT
            )
        }
    }

    private fun setQuestionsRepetitionObserver() {
        viewModel.questionsRepetitionState.observe(this) { state ->
            binding.questionsRepetitionChip.setOnCheckedChangeListener { _, _ -> }
            when (state) {
                QuestionsRepetitionState.NO_REPETITION -> {
                    binding.questionsRepetitionChip.isChecked = true
                }
                else -> {
                    binding.questionsRepetitionChip.isChecked = false
                }
            }

            setOnQuestionRepetitionCheckedChangeListener()
        }
    }


    private fun setOnQuestionRepetitionCheckedChangeListener() {
        binding.questionsRepetitionChip.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateQuestionsRepetition(this, QuestionsRepetitionState.NO_REPETITION)
            } else {
                viewModel.updateQuestionsRepetition(this, QuestionsRepetitionState.REPETITION)
            }
        }
    }

    private fun makeResetPassedQuestionsClickable() {
        val spannableString = SpannableString(binding.resetPassedQuestionsHint.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                ResetPassedQuestionsConfirmationDialog().show(supportFragmentManager, null)
            }
        }

        spannableString.setSpan(
            clickableSpan,
            0,
            binding.resetPassedQuestionsHint.text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.resetPassedQuestionsHint.text = spannableString
        binding.resetPassedQuestionsHint.movementMethod = BetterLinkMovementMethod.getInstance()
    }

    private fun makeDevelopersAndLicensesTextClickable() {
        val spannableString = SpannableString(binding.creatorsAndLicenses.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(this@SettingsActivity, DevelopersAndLicensesActivity::class.java)
                startActivity(intent)
            }
        }

        spannableString.setSpan(
            clickableSpan,
            0,
            binding.creatorsAndLicenses.text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            ),
            0,
            binding.creatorsAndLicenses.text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.creatorsAndLicenses.text = spannableString
        binding.creatorsAndLicenses.movementMethod = BetterLinkMovementMethod.getInstance()
    }
}