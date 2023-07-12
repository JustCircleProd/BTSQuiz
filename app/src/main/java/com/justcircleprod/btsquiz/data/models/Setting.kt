package com.justcircleprod.btsquiz.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "state") val state: Int
)