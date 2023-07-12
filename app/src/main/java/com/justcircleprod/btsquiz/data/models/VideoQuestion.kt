package com.justcircleprod.btsquiz.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_questions")
data class VideoQuestion(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo(name = "video_entry_name") val video_entry_name: String,
    @ColumnInfo(name = "first_option") override val firstOption: String,
    @ColumnInfo(name = "second_option") override val secondOption: String,
    @ColumnInfo(name = "third_option") override val thirdOption: String,
    @ColumnInfo(name = "fourth_option") override val fourthOption: String,
    @ColumnInfo(name = "answer_num") override val answerNum: Int,
    @ColumnInfo(name = "points") override val points: Int
) : Question