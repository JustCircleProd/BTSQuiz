package com.justcircleprod.btsquiz.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_questions")
data class ImageQuestion(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo(name = "image_entry_name") val image_entry_name: String,
    @ColumnInfo(name = "first_option") override val firstOption: String,
    @ColumnInfo(name = "second_option") override val secondOption: String,
    @ColumnInfo(name = "third_option") override val thirdOption: String,
    @ColumnInfo(name = "fourth_option") override val fourthOption: String,
    @ColumnInfo(name = "answer_num") override val answerNum: Int,
    @ColumnInfo(name = "points") override val points: Int
) : Question