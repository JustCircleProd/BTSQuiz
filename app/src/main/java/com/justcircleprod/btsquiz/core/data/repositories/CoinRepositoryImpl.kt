package com.justcircleprod.btsquiz.core.data.repositories

import androidx.datastore.preferences.core.stringPreferencesKey
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreConstants
import com.justcircleprod.btsquiz.core.data.dataStore.DataStoreManager
import com.justcircleprod.btsquiz.core.domain.repositories.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : CoinRepository {

    override fun getUserCoinsQuantity(): Flow<String?> =
        dataStoreManager.collect(stringPreferencesKey(DataStoreConstants.USER_COINS_QUANTITY_KEY))

    override suspend fun editUserCoinsQuantity(value: Int) {
        dataStoreManager.edit(
            stringPreferencesKey(DataStoreConstants.USER_COINS_QUANTITY_KEY),
            value.toString()
        )
    }

    override suspend fun addUserCoins(coinsToAdd: Int) {
        val currentCoins = getUserCoinsQuantity().first()?.toInt() ?: return
        editUserCoinsQuantity(currentCoins + coinsToAdd)
    }

    override suspend fun subtractUserCoins(coinsToSubtract: Int) {
        val currentCoins = getUserCoinsQuantity().first()?.toInt() ?: return
        editUserCoinsQuantity(currentCoins - coinsToSubtract)
    }
}