package com.justcircleprod.btsquiz.core.domain.repositories

import com.justcircleprod.btsquiz.core.data.models.Score

interface ScoreRepository {

    fun getScores(): List<Score>
}