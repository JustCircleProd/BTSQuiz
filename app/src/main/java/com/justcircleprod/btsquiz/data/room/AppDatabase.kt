package com.justcircleprod.btsquiz.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.justcircleprod.btsquiz.data.models.*
import com.justcircleprod.btsquiz.data.models.levels.LevelProgress
import com.justcircleprod.btsquiz.data.models.levels.LockedLevel
import com.justcircleprod.btsquiz.data.models.passedQuestion.PassedQuestion
import com.justcircleprod.btsquiz.data.models.questions.AudioQuestion
import com.justcircleprod.btsquiz.data.models.questions.ImageQuestion
import com.justcircleprod.btsquiz.data.models.questions.TextQuestion
import com.justcircleprod.btsquiz.data.models.questions.VideoQuestion
import com.justcircleprod.btsquiz.data.room.convertes.Converters
import com.justcircleprod.btsquiz.data.room.dao.*
import com.justcircleprod.btsquiz.data.room.migrations.MIGRATION_1_2
import com.justcircleprod.btsquiz.data.room.migrations.MIGRATION_2_3
import com.justcircleprod.btsquiz.data.room.migrations.MIGRATION_3_4
import com.justcircleprod.btsquiz.data.room.migrations.MIGRATION_4_5
import com.justcircleprod.btsquiz.data.room.migrations.MIGRATION_5_6
import com.justcircleprod.btsquiz.data.room.migrations.MIGRATION_6_7


@Database(
    version = 7,
    entities = [TextQuestion::class, ImageQuestion::class, AudioQuestion::class,
        VideoQuestion::class, PassedQuestion::class, Score::class, Setting::class, LockedLevel::class,
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
    abstract fun settingDao(): SettingDao
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
                    MIGRATION_6_7
                )
                .build()
    }
}