package com.example.gfit.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gfit.data.model.user.UserWorkoutPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// SharedViewModelProvider.kt
object SharedViewModelProvider {
    private var sharedViewModel: SharedViewModel? = null

    fun getSharedViewModel(context: Context): SharedViewModel {
        if (sharedViewModel == null) {
            sharedViewModel = ViewModelProvider(
                ViewModelOwner,
                defaultViewModelProviderFactory(context)
            )[SharedViewModel::class.java]
        }
        return sharedViewModel!!
    }

    private fun defaultViewModelProviderFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    }
}

class SharedViewModel : ViewModel() {
    private val _userPreferences = MutableStateFlow<UserWorkoutPreferences?>(null)
    val userPreferences = _userPreferences.asStateFlow()

    fun updatePreferences(updatedPreferences: UserWorkoutPreferences) {
        viewModelScope.launch {
            Log.d("SharedViewModel", "📤 Mise à jour des préférences: $updatedPreferences")
            _userPreferences.emit(updatedPreferences)
        }
    }
}