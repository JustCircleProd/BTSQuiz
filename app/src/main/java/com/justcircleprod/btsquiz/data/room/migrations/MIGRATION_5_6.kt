package com.justcircleprod.btsquiz.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_5_6: Migration = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS \"locked_levels\" (\"id\" INTEGER NOT NULL, \"price\" INTEGER NOT NULL, \"is_opened\" INT NOT NULL, PRIMARY KEY(\"id\"))"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO locked_levels (id, price, is_opened) VALUES (2, 900, 0)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO locked_levels (id, price, is_opened) VALUES (3, 1800, 0)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO locked_levels (id, price, is_opened) VALUES (4, 4050, 0)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO locked_levels (id, price, is_opened) VALUES (5, 4650, 0)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO locked_levels (id, price, is_opened) VALUES (6, 6600, 0)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO locked_levels (id, price, is_opened) VALUES (7, 7000, 0)"
        )


        database.execSQL(
            "CREATE TABLE IF NOT EXISTS \"levels_progress\" (\"id\" INTEGER NOT NULL, \"progress\" INTEGER NOT NULL, \"questions_quantity\" INTEGER NOT NULL, PRIMARY KEY(\"id\"))"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO levels_progress (id, progress, questions_quantity) VALUES (1, 0, 31)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO levels_progress (id, progress, questions_quantity) VALUES (2, 0, 46)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO levels_progress (id, progress, questions_quantity) VALUES (3, 0, 82)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO levels_progress (id, progress, questions_quantity) VALUES (4, 0, 78)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO levels_progress (id, progress, questions_quantity) VALUES (5, 0, 95)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO levels_progress (id, progress, questions_quantity) VALUES (6, 0, 84)"
        )

        database.execSQL(
            "INSERT OR IGNORE INTO levels_progress (id, progress, questions_quantity) VALUES (7, 0, 127)"
        )
    }
}