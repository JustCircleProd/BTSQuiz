package com.justcircleprod.btsquiz.core.data.dataStore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("bts_quiz_data")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    suspend fun edit(key: Preferences.Key<String>, value: String) {
        dataStore.edit {
            it[key] = value
        }
    }

    fun collect(key: Preferences.Key<String>) =
        dataStore.data.map {
            it[key]
        }
}