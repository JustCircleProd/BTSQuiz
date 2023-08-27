package com.justcircleprod.btsquiz.data.models.questions

interface Question {
    val id: Int
    var firstOption: String
    var secondOption: String
    var thirdOption: String
    var fourthOption: String
    val answerNum: Int
    val points: Int
}