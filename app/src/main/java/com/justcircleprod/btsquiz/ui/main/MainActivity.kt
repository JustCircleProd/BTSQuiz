package com.justcircleprod.btsquiz.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.ActivityMainBinding
import com.justcircleprod.btsquiz.ui.categories.CategoriesActivity
import com.justcircleprod.btsquiz.ui.results.ResultsActivity
import com.justcircleprod.btsquiz.ui.settings.SettingsActivity


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setOnClickListeners()

        setContentView(binding.root)
    }

    private fun setOnClickListeners() {
        binding.startQuiz.setOnClickListener { startCategoryActivity() }
        binding.settings.setOnClickListener { startSettingActivity() }
        binding.results.setOnClickListener { startResultsActivity() }
    }

    private fun startCategoryActivity() {
        val intent = Intent(this, CategoriesActivity::class.java)
        startActivity(intent)
    }

    private fun startSettingActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun startResultsActivity() {
        val intent = Intent(this, ResultsActivity::class.java)
        startActivity(intent)
    }
}