package com.justcircleprod.btsquiz.core.data.models.questions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "text_questions")
data class TextQuestion(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "first_option") override var firstOption: String,
    @ColumnInfo(name = "second_option") override var secondOption: String,
    @ColumnInfo(name = "third_option") override var thirdOption: String,
    @ColumnInfo(name = "fourth_option") override var fourthOption: String,
    @ColumnInfo(name = "answer_num") override val answerNum: Int,
    @ColumnInfo(name = "points") override val points: Int
) : Question