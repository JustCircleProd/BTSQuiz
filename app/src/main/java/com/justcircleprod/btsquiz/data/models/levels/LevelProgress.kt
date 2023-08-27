package com.justcircleprod.btsquiz.data.models.levels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "levels_progress")
data class LevelProgress(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "progress") val progress: Int,
    @ColumnInfo(name = "questions_quantity") val questionsQuantity: Int
)
