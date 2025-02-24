package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.gfit.data.model.user.UserWorkoutPreferences
import com.example.gfit.databinding.ActivityForm14Binding
import com.example.gfit.viewmodel.SharedViewModel
import com.example.gfit.viewmodel.SharedViewModelProvider

// FormActivity1.kt
class FormActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityForm14Binding
    private val sharedViewModel: SharedViewModel by lazy {
        SharedViewModelProvider.getSharedViewModel(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForm14Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            saveForm1Data()
            navigateToAfterForm()
        }
    }

    private fun saveForm1Data() {
        val userPreferences = UserWorkoutPreferences(
            name = binding.fullNameEdtView.text.toString(),
            age = binding.ageEdtView.text.toString().toInt(),
            sex = if (binding.maleRdG.isChecked) "Man" else "Woman",
            height = binding.heightEdtView.text.toString().toInt(),
            weight = binding.currentWeightEdtView.text.toString().toInt(),
            targetWeight = binding.targetWeightEdtView.text.toString().toIntOrNull(),
            goal = "Build Muscle", priority = "6 months", workoutFrequency = 3,
            maxSessionDuration = 45, hasEquipment = true,
            activityLevel = "5", sportsPracticed = "none", diet = "Normal",
            allergies = "none", cookingLevel = "basic", takesSupplements = false,
            obstacles = "time", motivation = "youtube"
        )
        sharedViewModel.updatePreferences(userPreferences)
        Log.d("FormActivity1", "✅ Données sauvegardées: $userPreferences")
    }

    private fun navigateToAfterForm(){
        Intent(this, AfterFormActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}
