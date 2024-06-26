package com.justcircleprod.btsquiz.core.data.repositories

import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.repositories.LockedLevelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LockedLevelRepositoryImp @Inject constructor(
    private val db: AppDatabase
) : LockedLevelRepository {

    override fun getLockedLevelFlow(levelId: Int) =
        db.lockedLevelDao().getFlow(levelId)

    override suspend fun unlockLevel(levelId: Int) {
        withContext(Dispatchers.IO) {
            db.lockedLevelDao().updateIsOpenedField(levelId, true)
        }
    }

    override suspend fun lockLevel(levelId: Int) {
        withContext(Dispatchers.IO) {
            db.lockedLevelDao().updateIsOpenedField(levelId, false)
        }
    }
}