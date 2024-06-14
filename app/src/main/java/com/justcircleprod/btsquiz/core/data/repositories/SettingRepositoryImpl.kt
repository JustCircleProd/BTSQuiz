package com.justcircleprod.btsquiz.core.data.repositories

import androidx.datastore.preferences.core.stringPreferencesKey
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreManager
import com.justcircleprod.btsquiz.core.domain.repositories.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : SettingRepository {

    override fun isIntroductionShown() =
        dataStoreManager.collect(stringPreferencesKey(DataStoreConstants.INTRODUCTION_IS_SHOWN_KEY))

    override suspend fun setIntroductionShown() {
        dataStoreManager.edit(
            stringPreferencesKey(DataStoreConstants.INTRODUCTION_IS_SHOWN_KEY),
            DataStoreConstants.INTRODUCTION_IS_SHOWN
        )
    }

    override fun getWithoutQuizHintsState() =
        dataStoreManager.collect(stringPreferencesKey(DataStoreConstants.WITHOUT_QUIZ_HINTS_KEY))

    override suspend fun editWithoutQuizHintsState(state: String) {
        dataStoreManager.edit(
            stringPreferencesKey(DataStoreConstants.WITHOUT_QUIZ_HINTS_KEY),
            state
        )
    }
}