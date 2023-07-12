package com.justcircleprod.btsquiz.data.room.constants

enum class DifficultyState(val value: Int) {
    RANDOM(0),
    USUAL(1),
    DIFFICULT(2);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}