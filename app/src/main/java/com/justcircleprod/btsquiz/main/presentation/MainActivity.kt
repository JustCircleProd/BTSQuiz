package com.justcircleprod.btsquiz.main.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.databinding.ActivityMainBinding
import com.justcircleprod.btsquiz.introduction.presentation.IntroductionActivity
import com.justcircleprod.btsquiz.levels.presentation.LevelsActivity
import com.justcircleprod.btsquiz.settings.presentation.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private var shouldStartIntroductionActivityCollectionJob: Job? = null

    // to set up the collectors again, but not to execute code in them
    private var isIntroductionShownCollectorStopped = false
    private var isShouldStartIntroductionActivityCollectorStopped = false

    override fun onStop() {
        super.onStop()

        isIntroductionShownCollectorStopped = true
        isShouldStartIntroductionActivityCollectorStopped = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // DataStoreConstants.INTRODUCTION_IS_SHOWN or null will be the last value
                viewModel.isIntroductionShown.transformWhile {
                    emit(it)
                    it != DataStoreConstants.INTRODUCTION_IS_SHOWN && it != null
                }.collect { isIntroductionShown ->
                    if (isIntroductionShownCollectorStopped) {
                        isIntroductionShownCollectorStopped = false
                        return@collect
                    }

                    when (isIntroductionShown) {
                        DataStoreConstants.INTRODUCTION_IS_SHOWN -> {
                            setOnClickListeners()
                            setContentView(binding.root)
                        }

                        null -> {
                            viewModel.setIntroductionShown()

                            if (shouldStartIntroductionActivityCollectionJob != null) return@collect

                            shouldStartIntroductionActivityCollectionJob = lifecycleScope.launch {
                                repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    viewModel.shouldStartIntroductionActivity.collect collect2@ {
                                        if (isShouldStartIntroductionActivityCollectorStopped) {
                                            isShouldStartIntroductionActivityCollectorStopped = false
                                            return@collect2
                                        }

                                        if (it) {
                                            startIntroductionActivity()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.playBtn.setOnClickListener { startLevelsActivity() }
        binding.settingsBtn.setOnClickListener { startSettingActivity() }
    }

    private fun startIntroductionActivity() {
        val intent = Intent(this, IntroductionActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startLevelsActivity() {
        val intent = Intent(this, LevelsActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startSettingActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
        finish()
    }
}