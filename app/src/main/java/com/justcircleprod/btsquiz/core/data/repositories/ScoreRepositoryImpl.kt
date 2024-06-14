package com.justcircleprod.btsquiz.core.data.repositories

import com.justcircleprod.btsquiz.core.data.models.Score
import com.justcircleprod.btsquiz.core.data.room.AppDatabase
import com.justcircleprod.btsquiz.core.domain.repositories.ScoreRepository
import javax.inject.Inject

class ScoreRepositoryImpl @Inject constructor(private val db: AppDatabase) : ScoreRepository {

    override fun getScores(): List<Score> = db.scoreDao().getAll()
}