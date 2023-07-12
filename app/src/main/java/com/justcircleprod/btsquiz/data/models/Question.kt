package com.justcircleprod.btsquiz.data.models

interface Question {
    val id: Int
    val firstOption: String
    val secondOption: String
    val thirdOption: String
    val fourthOption: String
    val answerNum: Int
    val points: Int
}