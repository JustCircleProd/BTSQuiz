package com.justcircleprod.btsquiz.levels.presentation.levelAdapter

import com.justcircleprod.btsquiz.core.data.constants.LevelConstants

// if isOpened == null, then price and progress == 0
// means it is LEVEL_PASSED_QUESTIONS
data class LevelItem(
    val levelId: Int,
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

            for (i in LevelConstants.LEVEL_1_ID..LevelConstants.LEVEL_7_ID) {
                levelItemPlaceholders.add(
                    LevelItem(
                        levelId = i,
                        isOpened = null,
                        price = 0,
                        progress = 0,
                        questionNumber = 0,
                        isPlaceholder = true
                    )
                )
            }

            return levelItemPlaceholders.toList()
        }
    }
}