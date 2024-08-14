package com.justcircleprod.btsquiz.core.data.constants

object LevelConstants {

    const val LEVEL_PASSED_QUESTIONS_ID = 0
    const val LEVEL_1_ID = 1
    const val LEVEL_2_ID = 2
    const val LEVEL_3_ID = 3
    const val LEVEL_4_ID = 4
    const val LEVEL_5_ID = 5
    const val LEVEL_6_ID = 6
    const val LEVEL_7_ID = 7

    private const val LEVEL_PASSED_QUESTIONS_QUESTION_WORTH = 20
    private const val LEVEL_1_QUESTION_WORTH = 30
    private const val LEVEL_2_QUESTION_WORTH = 40
    const val LEVEL_3_QUESTION_WORTH = 50
    private const val LEVEL_4_QUESTION_WORTH = 60
    private const val LEVEL_5_QUESTION_WORTH = 70
    private const val LEVEL_6_QUESTION_WORTH = 80
    private const val LEVEL_7_QUESTION_WORTH = 90

    fun getQuestionWorth(levelId: Int): Int {
        return when (levelId) {
            LEVEL_PASSED_QUESTIONS_ID -> LEVEL_PASSED_QUESTIONS_QUESTION_WORTH
            LEVEL_1_ID -> LEVEL_1_QUESTION_WORTH
            LEVEL_2_ID -> LEVEL_2_QUESTION_WORTH
            LEVEL_3_ID -> LEVEL_3_QUESTION_WORTH
            LEVEL_4_ID -> LEVEL_4_QUESTION_WORTH
            LEVEL_5_ID -> LEVEL_5_QUESTION_WORTH
            LEVEL_6_ID -> LEVEL_6_QUESTION_WORTH
            LEVEL_7_ID -> LEVEL_7_QUESTION_WORTH
            else -> LEVEL_PASSED_QUESTIONS_QUESTION_WORTH
        }
    }
}