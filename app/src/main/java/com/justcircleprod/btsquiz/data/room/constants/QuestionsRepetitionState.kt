package com.justcircleprod.btsquiz.data.room.constants

enum class QuestionsRepetitionState(val value: Int) {
    REPETITION(0),
    NO_REPETITION(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}