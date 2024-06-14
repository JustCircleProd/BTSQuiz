package com.justcircleprod.btsquiz.di

import android.content.Context
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreManager
import com.justcircleprod.btsquiz.core.data.repositories.CoinRepositoryImpl
import com.justcircleprod.btsquiz.core.data.repositories.LevelProgressRepositoryImpl
import com.justcircleprod.btsquiz.core.data.repositories.LockedLevelRepositoryImp
import com.justcircleprod.btsquiz.core.data.repositories.PassedQuestionRepositoryImpl
import com.justcircleprod.btsquiz.core.data.repositories.QuestionRepositoryImpl
import com.justcircleprod.btsquiz.core.data.repositories.ScoreRepositoryImpl
import com.justcircleprod.btsquiz.core.data.repositories.SettingRepositoryImpl
import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LevelProgressRepository
import com.justcircleprod.btsquiz.core.domain.repositories.LockedLevelRepository
import com.justcircleprod.btsquiz.core.domain.repositories.PassedQuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.QuestionRepository
import com.justcircleprod.btsquiz.core.domain.repositories.ScoreRepository
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) =
        DataStoreManager(context)

    @Singleton
    @Provides
    fun provideCoinRepository(dataStoreManager: DataStoreManager): CoinRepository =
        CoinRepositoryImpl(dataStoreManager)

    @Singleton
    @Provides
    fun provideLevelProgressRepository(db: AppDatabase): LevelProgressRepository =
        LevelProgressRepositoryImpl(db)

    @Singleton
    @Provides
    fun provideLockedLevelRepository(db: AppDatabase): LockedLevelRepository =
        LockedLevelRepositoryImp(db)

    @Singleton
    @Provides
    fun providePassedQuestionRepository(db: AppDatabase): PassedQuestionRepository =
        PassedQuestionRepositoryImpl(db)

    @Singleton
    @Provides
    fun provideQuestionRepository(db: AppDatabase): QuestionRepository =
        QuestionRepositoryImpl(db)

    @Singleton
    @Provides
    fun provideScoreRepository(db: AppDatabase): ScoreRepository =
        ScoreRepositoryImpl(db)

    @Singleton
    @Provides
    fun provideSettingRepository(dataStoreManager: DataStoreManager): SettingRepository =
        SettingRepositoryImpl(dataStoreManager)
}