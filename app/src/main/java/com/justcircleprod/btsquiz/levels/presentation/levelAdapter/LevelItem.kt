package com.justcircleprod.btsquiz.levels.presentation.levelAdapter

import androidx.annotation.StringRes
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.core.data.constants.LevelConstants

// if isOpened == null, then price and progress == 0
// means it is LEVEL_PASSED_QUESTIONS
data class LevelItem(
    val levelId: Int,
    @StringRes val nameStringResId: Int,
    val isOpened: Boolean?,
    val price: Int,
    val progress: Int,
    val questionNumber: Int,
    val isPlaceholder: Boolean = false
) {
    companion object {
        // Field values do not matter
        fun getPlaceholders(): List<LevelItem> {
            val levelItemPlaceholders = mutableListOf<LevelItem>()

            levelItemPlaceholders.add(
                LevelItem(
                    levelId = LevelConstants.LEVEL_7_ID + 1,
                    nameStringResId = R.string.loading_levels,
                    isOpened = null,
                    price = 0,
                    progress = 0,
                    questionNumber = 0,
                    isPlaceholder = true
                )
            )

            return levelItemPlaceholders.toList()
        }

        fun getNameStringResId(levelId: Int): Int {
            return when (levelId) {
                LevelConstants.LEVEL_PASSED_QUESTIONS_ID -> R.string.passed_questions_level_name
                LevelConstants.LEVEL_1_ID -> R.string.level_1_name
                LevelConstants.LEVEL_2_ID -> R.string.level_2_name
                LevelConstants.LEVEL_3_ID -> R.string.level_3_name
                LevelConstants.LEVEL_4_ID -> R.string.level_4_name
                LevelConstants.LEVEL_5_ID -> R.string.level_5_name
                LevelConstants.LEVEL_6_ID -> R.string.level_6_name
                LevelConstants.LEVEL_7_ID -> R.string.level_7_name
                else -> R.string.loading_levels
            }
        }
    }
}