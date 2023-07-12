package com.justcircleprod.btsquiz.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.justcircleprod.btsquiz.R
import com.justcircleprod.btsquiz.data.AppRepository
import com.justcircleprod.btsquiz.data.models.Setting
import com.justcircleprod.btsquiz.data.room.constants.AppDatabaseConstants
import com.justcircleprod.btsquiz.data.room.constants.DifficultyState
import com.justcircleprod.btsquiz.data.room.constants.QuestionsRepetitionState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    val difficultyState = MutableLiveData<DifficultyState>()
    val questionsRepetitionState = MutableLiveData<QuestionsRepetitionState>()

    init {
        viewModelScope.launch {
            difficultyState.postValue(
                DifficultyState.fromInt(
                    repository.getSetting(AppDatabaseConstants.DIFFICULTY_SETTING_ID).state
                )
            )
            questionsRepetitionState.postValue(
                QuestionsRepetitionState.fromInt(
                    repository.getSetting(AppDatabaseConstants.QUESTIONS_REPETITION_SETTING_ID).state
                )
            )
        }
    }

    fun updateDifficulty(context: Context, state: DifficultyState) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateSetting(
                    Setting(
                        AppDatabaseConstants.DIFFICULTY_SETTING_ID, state.value
                    )
                )
            }
            Toast.makeText(
                context,
                context.resources.getString(R.string.set_difficulty),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateQuestionsRepetition(context: Context, state: QuestionsRepetitionState) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateSetting(
                    Setting(
                        AppDatabaseConstants.QUESTIONS_REPETITION_SETTING_ID, state.value
                    )
                )
            }

            val toastMessage = if (state == QuestionsRepetitionState.NO_REPETITION) {
                context.resources.getString(R.string.set_no_questions_repetition)
            } else {
                context.resources.getString(R.string.unset_no_questions_repetition)
            }
            Toast.makeText(
                context,
                toastMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun deleteAllPassedQuestions(context: Context) {
        viewModelScope.launch {
            val deletedCount = withContext(Dispatchers.IO) {
                repository.deleteAllPassedQuestions()
            }

            val toastMessage = context.getString(R.string.reset_n_passed_question, deletedCount)

            Toast.makeText(
                context,
                toastMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}