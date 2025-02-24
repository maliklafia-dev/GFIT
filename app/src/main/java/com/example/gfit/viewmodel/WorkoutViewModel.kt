package com.example.gfit.viewmodel

import com.example.gfit.repositories.WorkoutRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gfit.data.model.user.UserWorkoutPreferences
import com.example.gfit.data.model.workout.WorkoutExercise
import com.example.gfit.data.network.dto.user_dto.UserInfos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WorkoutState {
    data object Loading : WorkoutState()
    data class Success(val exercises: List<WorkoutExercise>) : WorkoutState()
    data class Error(val message: String) : WorkoutState()
}

sealed class PreferenceState {
    data object Loading : PreferenceState()
    data class Success(val preferences: UserWorkoutPreferences) : PreferenceState()
    data class Error(val message: String) : PreferenceState()
}

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _workoutState = MutableStateFlow<WorkoutState>(WorkoutState.Loading)
    val workoutState: StateFlow<WorkoutState> = _workoutState

    private val _userInfos = MutableLiveData<PreferenceState?>()
    val userInfos: LiveData<PreferenceState?> get() = _userInfos

    private val _infos = MutableLiveData<UserWorkoutPreferences?>()
    val infos: LiveData<UserWorkoutPreferences?> get() = _infos

    fun generateWorkoutPlan(preferences: UserWorkoutPreferences, userId: String) {
        // Vérifier si les données sont complètes avant d'appeler l'API
        if (!isPreferencesComplete(preferences)) {
            _workoutState.value = WorkoutState.Error("Données incomplètes")
            Log.e("WorkoutViewModel", "❌ Données incomplètes: $preferences")
            return
        }
        viewModelScope.launch {
            try {
                Log.d("WorkoutViewModel", "🚀 Début de la génération du programme")
                _workoutState.value = WorkoutState.Loading

                Log.d("WorkoutViewModel", "📤 Appel au repository avec les préférences: $preferences")
                val exercises = repository.fetchWorkoutPlan(preferences)

                if (exercises.isNotEmpty()) {
                    Log.d("WorkoutViewModel", "✅ Exercices reçus: ${exercises.size}")
                    repository.saveWorkoutProgram(userId, exercises, preferences)
                    _workoutState.value = WorkoutState.Success(exercises)
                } else {
                    Log.d("WorkoutViewModel", "⚠️ Aucun exercice généré")
                    _workoutState.value = WorkoutState.Error("Aucun exercice n'a été généré")
                }
            } catch (e: Exception) {
                Log.e("WorkoutViewModel", "❌ Erreur: ${e.message}", e)
                _workoutState.value = WorkoutState.Error(e.message ?: "Une erreur inconnue est survenue")
            }
        }
    }

    private fun isPreferencesComplete(preferences: UserWorkoutPreferences): Boolean {
        return with(preferences) {
            name.isNotBlank() &&
                    age > 0 &&
                    sex.isNotBlank() &&
                    height > 0 &&
                    weight > 0 &&
                    goal.isNotBlank() &&
                    workoutFrequency > 0 &&
                    maxSessionDuration > 0
        }
    }

    fun loadSavedWorkoutProgram(userId: String) {
        viewModelScope.launch {
            try {
                _workoutState.value = WorkoutState.Loading
                val savedExercises = repository.getLatestWorkoutProgram(userId)

                if (savedExercises != null) {
                    _workoutState.value = WorkoutState.Success(savedExercises)
                } else {
                    _workoutState.value = WorkoutState.Error("Aucun programme sauvegardé")
                }
            } catch (e: Exception) {
                _workoutState.value = WorkoutState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }

    fun fetchUserInfos(userId: String) {

        viewModelScope.launch {
            try {
                _userInfos.value = PreferenceState.Loading
                val savedPreferences = repository.getUserPreferences(userId)
                if (savedPreferences != null) {
                    _userInfos.value = PreferenceState.Success(savedPreferences)
                    _infos.postValue(savedPreferences)
                }else {
                    _userInfos.value = PreferenceState.Error("Utilisateur non trouvé")
                }
            } catch (e: Exception) {
                _userInfos.value = PreferenceState.Error(e.message ?: "Erreur inconnue")
            }
        }
    }
}