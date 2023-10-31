package com.justcircleprod.btsquiz.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_7_8: Migration = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 800 WHERE id = 2"
        )

        database.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 1700 WHERE id = 3"
        )

        database.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 4000 WHERE id = 4"
        )

        database.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 4500 WHERE id = 5"
        )

        database.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 6500 WHERE id = 6"
        )

        database.execSQL(
            "UPDATE OR IGNORE locked_levels SET price = 7000 WHERE id = 7"
        )
    }
}