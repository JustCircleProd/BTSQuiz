package com.justcircleprod.btsquiz.main.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.databinding.ActivityMainBinding
import com.justcircleprod.btsquiz.introduction.presentation.IntroductionActivity
import com.justcircleprod.btsquiz.levels.presentation.LevelsActivity
import com.justcircleprod.btsquiz.settings.presentation.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            if (viewModel.isIntroductionShown.first() == DataStoreConstants.INTRODUCTION_IS_SHOWN) {
                setOnClickListeners()
                setContentView(binding.root)
            } else {
                startIntroductionActivity()
                viewModel.setIntroductionShown()
                finish()
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