package com.justcircleprod.btsquiz.ui.introduction

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.databinding.ActivityIntroductionBinding
import com.justcircleprod.btsquiz.ui.introduction.introductionCardAdapter.HorizontalMarginItemDecoration
import com.justcircleprod.btsquiz.ui.introduction.introductionCardAdapter.IntroductionCardAdapter
import com.justcircleprod.btsquiz.ui.introduction.introductionCardAdapter.IntroductionCardResources
import com.justcircleprod.btsquiz.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class IntroductionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroductionBinding
    private val viewModel: IntroductionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)

        setupViewPager()
        setContentView(binding.root)
    }

    private fun setupViewPager() {
        val cardResources = listOf(
            IntroductionCardResources(
                R.string.introduction_card_1_title,
                R.string.introduction_card_1_text,
                R.drawable.introduction_card_1
            ),
            IntroductionCardResources(
                R.string.introduction_card_2_title,
                R.string.introduction_card_2_text,
                R.drawable.introduction_card_2
            ),
            IntroductionCardResources(
                R.string.introduction_card_3_title,
                R.string.introduction_card_3_text,
                R.drawable.introduction_card_3
            )
        )

        val adapter = IntroductionCardAdapter(
            cardResources = cardResources,
            onNextBtnClicked = {
                binding.viewPager.currentItem++
            },
            onPlayBtnClicked = {
                lifecycleScope.launch {
                    viewModel.setIntroductionShown()
                    startMainActivity()
                }
            }
        )

        // for previous/next card effect

        binding.viewPager.offscreenPageLimit = 1

        val nextItemVisiblePx =
            resources.getDimension(R.dimen.introduction_viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.introduction_viewpager_current_item_horizontal_margin)

        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * abs(position))

            // dynamic height calculation for each item
            val wMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(page.width, View.MeasureSpec.EXACTLY)
            val hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            page.measure(wMeasureSpec, hMeasureSpec)
            binding.viewPager.layoutParams =
                (binding.viewPager.layoutParams).also { lp -> lp.height = page.measuredHeight }
            binding.viewPager.invalidate()
        }
        binding.viewPager.setPageTransformer(pageTransformer)

        val itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.introduction_viewpager_current_item_horizontal_margin
        )
        binding.viewPager.addItemDecoration(itemDecoration)

        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}