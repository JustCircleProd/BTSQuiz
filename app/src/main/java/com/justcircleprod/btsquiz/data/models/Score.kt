package com.justcircleprod.btsquiz.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "score") val score: Int
)