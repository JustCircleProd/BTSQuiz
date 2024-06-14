package com.justcircleprod.btsquiz.core.data.room.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3: Migration = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "UPDATE OR IGNORE text_questions SET first_option = \"Jimin, Suga\" WHERE id = 204"
        )

        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET fourth_option = \"TAEYANG x Jimin - 'VIBE'\" WHERE id = 89"
        )

        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET fourth_option = \"'TAEYANG x Jimin - 'VIBE'\" WHERE id = 91"
        )

        db.execSQL(
            "UPDATE OR IGNORE audio_questions SET second_option = \"'TAEYANG x Jimin - 'VIBE'\" WHERE id = 92"
        )
    }
}