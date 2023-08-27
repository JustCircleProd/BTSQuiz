package com.justcircleprod.btsquiz.data.room.convertes

import androidx.room.TypeConverter
import com.justcircleprod.btsquiz.data.models.passedQuestion.QuestionContentType

class Converters {
    @TypeConverter
    fun toPassedQuestionContentType(value: String) = QuestionContentType.fromString(value)

    @TypeConverter
    fun fromPassedQuestionContentType(value: QuestionContentType) = value.toString()
}