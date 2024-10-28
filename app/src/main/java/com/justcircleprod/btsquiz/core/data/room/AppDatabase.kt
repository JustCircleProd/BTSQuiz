package com.justcircleprod.btsquiz.core.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.justcircleprod.btsquiz.core.data.models.Score
import com.justcircleprod.btsquiz.core.data.models.levels.LevelProgress
import com.justcircleprod.btsquiz.core.data.models.levels.LockedLevel
import com.justcircleprod.btsquiz.core.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.ImageQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.core.data.models.questions.VideoQuestion
import com.justcircleprod.btsquiz.core.data.room.convertes.Converters
import com.justcircleprod.btsquiz.core.data.room.dao.AudioQuestionDao
import com.justcircleprod.btsquiz.core.data.room.dao.ImageQuestionDao
import com.justcircleprod.btsquiz.core.data.room.dao.LevelProgressDao
import com.justcircleprod.btsquiz.core.data.room.dao.LockedLevelDao
import com.justcircleprod.btsquiz.core.data.room.dao.PassedQuestionDao
import com.justcircleprod.btsquiz.core.data.room.dao.ScoreDao
import com.justcircleprod.btsquiz.core.data.room.dao.TextQuestionDao
import com.justcircleprod.btsquiz.core.data.room.dao.VideoQuestionDao
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_10_11
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_11_12
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_12_13
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_1_2
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_2_3
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_3_4
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_4_5
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_5_6
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_6_7
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_7_8
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_8_9
import com.justcircleprod.btsquiz.core.data.room.migrations.MIGRATION_9_10


@Database(
    version = 13,
    entities = [TextQuestion::class, ImageQuestion::class, AudioQuestion::class,
        VideoQuestion::class, PassedQuestion::class, Score::class, LockedLevel::class,
        LevelProgress::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun textQuestionDao(): TextQuestionDao
    abstract fun imageQuestionDao(): ImageQuestionDao
    abstract fun audioQuestionDao(): AudioQuestionDao
    abstract fun videoQuestionDao(): VideoQuestionDao
    abstract fun passedQuestionDao(): PassedQuestionDao
    abstract fun scoreDao(): ScoreDao
    abstract fun lockedLevelDao(): LockedLevelDao
    abstract fun levelProgressDao(): LevelProgressDao

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "db.db")
                .createFromAsset("database/db.db")
                .addMigrations(
                    MIGRATION_1_2,
                    MIGRATION_2_3,
                    MIGRATION_3_4,
                    MIGRATION_4_5,
                    MIGRATION_5_6,
                    MIGRATION_6_7,
                    MIGRATION_7_8,
                    MIGRATION_8_9,
                    MIGRATION_9_10,
                    MIGRATION_10_11,
                    MIGRATION_11_12,
                    MIGRATION_12_13
                )
                .build()
    }
}