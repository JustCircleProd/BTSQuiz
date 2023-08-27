package com.justcircleprod.btsquiz.data.models.levels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locked_levels")
data class LockedLevel(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "is_opened") val isOpened: Boolean
)
