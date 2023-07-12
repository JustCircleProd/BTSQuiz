package com.justcircleprod.btsquiz.ui.categories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.justcircleprod.btsquiz.data.AppRepository
import com.justcircleprod.btsquiz.data.room.constants.AppDatabaseConstants
import com.justcircleprod.btsquiz.data.room.constants.DifficultyState
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    suspend fun getDifficultyState() = withContext(Dispatchers.IO) {
        DifficultyState.fromInt(
            repository.getSetting(AppDatabaseConstants.DIFFICULTY_SETTING_ID).state
        )
    }
}