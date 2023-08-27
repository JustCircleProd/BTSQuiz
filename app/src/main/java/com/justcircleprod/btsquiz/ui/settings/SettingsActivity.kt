package com.justcircleprod.btsquiz.ui.settings

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.databinding.ActivitySettingsBinding
import com.justcircleprod.btsquiz.ui.main.MainActivity
import com.justcircleprod.btsquiz.ui.settings.developersAndLicenses.DevelopersAndLicensesActivity
import com.justcircleprod.btsquiz.ui.settings.resetProgressConfirmationDialog.ResetProgressConfirmationDialog
import com.justcircleprod.btsquiz.ui.settings.resetProgressConfirmationDialog.ResetProgressConfirmationDialogCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.saket.bettermovementmethod.BetterLinkMovementMethod

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), ResetProgressConfirmationDialogCallback {
    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var reviewManager: ReviewManager

    override fun onResetProgressBtnClicked() {
        viewModel.resetProgress()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)

        onBackPressedDispatcher.addCallback { startMainActivity() }
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        setWithoutQuizHintsStateObserver()
        setOnResetProgressClickListener()

        setOnShareAppBtnClickListener()
        setOnRateAppBtnClickListener()

        makeDevelopersAndLicensesTextClickable()


        setContentView(binding.root)
    }

    private fun setWithoutQuizHintsStateObserver() {
        lifecycleScope.launch {
            viewModel.withoutQuizHints.collect {
                binding.withoutQuizHintsSwitch.setOnCheckedChangeListener { _, _ -> }

                when (it) {
                    DataStoreConstants.WITHOUT_QUIZ_HINTS -> {
                        binding.withoutQuizHintsSwitch.isChecked = true
                    }

                    else -> {
                        binding.withoutQuizHintsSwitch.isChecked = false
                    }
                }

                setOnWithoutQuizHintsSwitchCheckedChangeListener()
            }
        }
    }

    private fun setOnWithoutQuizHintsSwitchCheckedChangeListener() {
        binding.withoutQuizHintsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateWithoutQuizHintsState(
                if (isChecked) {
                    DataStoreConstants.WITHOUT_QUIZ_HINTS
                } else {
                    DataStoreConstants.WITH_QUIZ_HINTS
                }
            )
        }
    }

    private fun setOnResetProgressClickListener() {
        binding.resetProgressBtn.setOnClickListener {
            ResetProgressConfirmationDialog().show(supportFragmentManager, null)
        }
    }

    private fun setOnRateAppBtnClickListener() {
        binding.rateAppBtn.setOnClickListener {
            reviewManager = ReviewManagerFactory.create(this)

            val request = reviewManager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reviewManager.launchReviewFlow(this, task.result)
                }
            }
        }
    }

    private fun setOnShareAppBtnClickListener() {
        binding.shareAppBtn.setOnClickListener {
            lifecycleScope.launch {
                val passedQuestionCount = withContext(Dispatchers.IO) {
                    viewModel.getPassedQuestionCount()
                }

                val resultStr =
                    getString(
                        R.string.for_sharing_result,
                        passedQuestionCount
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
    }

    private fun makeDevelopersAndLicensesTextClickable() {
        val spannableString = SpannableString(binding.creatorsAndLicenses.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent =
                    Intent(this@SettingsActivity, DevelopersAndLicensesActivity::class.java)
                startActivity(intent)
                finish()
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

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}