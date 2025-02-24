package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gfit.databinding.ActivityForm34Binding
import com.example.gfit.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

// FormActivity3.kt
class FormActivity3 : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityForm34Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForm34Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            saveForm3Data()
            navigateToFormActivity4()
        }
    }

    private fun navigateToFormActivity4() {
        Intent(this, FormActivity4::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun saveForm3Data() {
        lifecycleScope.launch {
            sharedViewModel.userPreferences.value?.let { currentPrefs ->
                val updatedPreferences = currentPrefs.copy(
                    activityLevel = binding.dailyActivityLevelEdtView.text.toString(),
                    sportsPracticed = binding.sportPracticedEdtView.text.toString()
                        .takeIf { it.isNotEmpty() },
                    diet = binding.dietEdtText.text.toString(),
                    allergies = binding.allergiesEdtView.text.toString()
                        .takeIf { it.isNotEmpty() },
                    cookingLevel = binding.cookingLevelEdtView.text.toString()
                )
                sharedViewModel.updatePreferences(updatedPreferences)
                Log.d("FormActivity3", "$updatedPreferences")
            }
        }
    }


}
