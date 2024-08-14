package com.justcircleprod.btsquiz.core.data.constants

object CoinConstants {

    const val INITIAL_COINS_QUANTITY = 200

    private const val HINT_50_50_PRICE_LEVEL_PASSED_QUESTIONS = 30
    private const val HINT_50_50_PRICE_LEVEL_1 = 40
    private const val HINT_50_50_PRICE_LEVEL_2 = 50
    private const val HINT_50_50_PRICE_LEVEL_3 = 60
    private const val HINT_50_50_PRICE_LEVEL_4 = 70
    private const val HINT_50_50_PRICE_LEVEL_5 = 80
    private const val HINT_50_50_PRICE_LEVEL_6 = 90
    private const val HINT_50_50_PRICE_LEVEL_7 = 100

    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_PASSED_QUESTIONS = 60
    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_1 = 80
    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_2 = 100
    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_3 = 120
    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_4 = 140
    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_5 = 160
    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_6 = 180
    private const val HINT_CORRECT_ANSWER_PRICE_LEVEL_7 = 200

    private const val REWARDED_AD_WORTH_LEVEL_1 = 110
    private const val REWARDED_AD_WORTH_LEVEL_2 = 150
    private const val REWARDED_AD_WORTH_LEVEL_3 = 180
    private const val REWARDED_AD_WORTH_LEVEL_4 = 220
    private const val REWARDED_AD_WORTH_LEVEL_5 = 250
    private const val REWARDED_AD_WORTH_LEVEL_6 = 290
    private const val REWARDED_AD_WORTH_LEVEL_7 = 330

    fun getHint5050Price(levelId: Int): Int {
        return when (levelId) {
            LevelConstants.LEVEL_PASSED_QUESTIONS_ID -> HINT_50_50_PRICE_LEVEL_PASSED_QUESTIONS
            LevelConstants.LEVEL_1_ID -> HINT_50_50_PRICE_LEVEL_1
            LevelConstants.LEVEL_2_ID -> HINT_50_50_PRICE_LEVEL_2
            LevelConstants.LEVEL_3_ID -> HINT_50_50_PRICE_LEVEL_3
            LevelConstants.LEVEL_4_ID -> HINT_50_50_PRICE_LEVEL_4
            LevelConstants.LEVEL_5_ID -> HINT_50_50_PRICE_LEVEL_5
            LevelConstants.LEVEL_6_ID -> HINT_50_50_PRICE_LEVEL_6
            LevelConstants.LEVEL_7_ID -> HINT_50_50_PRICE_LEVEL_7
            else -> HINT_50_50_PRICE_LEVEL_PASSED_QUESTIONS
        }
    }

    fun getHintCorrectAnswerPrice(levelId: Int): Int {
        return when (levelId) {
            LevelConstants.LEVEL_PASSED_QUESTIONS_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_PASSED_QUESTIONS
            LevelConstants.LEVEL_1_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_1
            LevelConstants.LEVEL_2_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_2
            LevelConstants.LEVEL_3_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_3
            LevelConstants.LEVEL_4_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_4
            LevelConstants.LEVEL_5_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_5
            LevelConstants.LEVEL_6_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_6
            LevelConstants.LEVEL_7_ID -> HINT_CORRECT_ANSWER_PRICE_LEVEL_7
            else -> HINT_CORRECT_ANSWER_PRICE_LEVEL_PASSED_QUESTIONS
        }
    }

    fun getRewardedAdWorth(levelId: Int): Int {
        return when (levelId) {
            LevelConstants.LEVEL_1_ID -> REWARDED_AD_WORTH_LEVEL_1
            LevelConstants.LEVEL_2_ID -> REWARDED_AD_WORTH_LEVEL_2
            LevelConstants.LEVEL_3_ID -> REWARDED_AD_WORTH_LEVEL_3
            LevelConstants.LEVEL_4_ID -> REWARDED_AD_WORTH_LEVEL_4
            LevelConstants.LEVEL_5_ID -> REWARDED_AD_WORTH_LEVEL_5
            LevelConstants.LEVEL_6_ID -> REWARDED_AD_WORTH_LEVEL_6
            LevelConstants.LEVEL_7_ID -> REWARDED_AD_WORTH_LEVEL_7
            else -> REWARDED_AD_WORTH_LEVEL_1
        }
    }
}