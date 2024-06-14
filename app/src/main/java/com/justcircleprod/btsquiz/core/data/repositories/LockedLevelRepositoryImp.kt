package com.justcircleprod.btsquiz.core.data.repositories

import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.repositories.LockedLevelRepository
import javax.inject.Inject

class LockedLevelRepositoryImp @Inject constructor(
    private val db: AppDatabase
) : LockedLevelRepository {

    override fun getLockedLevelLiveData(levelId: Int) =
        db.lockedLevelDao().getLiveData(levelId)

    override suspend fun unlockLevel(levelId: Int) {
        db.lockedLevelDao().updateIsOpenedField(levelId, true)
    }

    override suspend fun lockLevel(levelId: Int) {
        db.lockedLevelDao().updateIsOpenedField(levelId, false)
    }
}