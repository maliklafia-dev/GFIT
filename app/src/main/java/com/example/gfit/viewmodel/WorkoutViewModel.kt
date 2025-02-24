package com.example.gfit.viewmodel

import com.example.gfit.repositories.WorkoutRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gfit.data.model.user.UserWorkoutPreferences
import com.example.gfit.data.model.workout.WorkoutExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class WorkoutState {
    data object Loading : WorkoutState()
    data class Success(val exercises: List<WorkoutExercise>) : WorkoutState()
    data class Error(val message: String) : WorkoutState()
}

class WorkoutViewModel(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _workoutState = MutableStateFlow<WorkoutState>(WorkoutState.Loading)
    val workoutState: StateFlow<WorkoutState> = _workoutState

    fun generateWorkoutPlan(preferences: UserWorkoutPreferences) {
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
}