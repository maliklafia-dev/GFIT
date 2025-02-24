package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gfit.databinding.ActivityForm24Binding
import com.example.gfit.viewmodel.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// FormActivity2.kt
class FormActivity2 : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityForm24Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForm24Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            saveForm2Data()
            navigateToFormActivity3()
        }
    }

    private fun saveForm2Data() {
        lifecycleScope.launch {
            sharedViewModel.userPreferences.value?.let { currentPrefs ->
                val updatedPreferences = currentPrefs.copy(
                    goal = binding.physicalGoalEdtView.text.toString(),
                    priority = binding.priorityDeadlineEdtView.text.toString(),
                    workoutFrequency = binding.workoutFrequencyEdtView.text.toString().toInt(),
                    maxSessionDuration = binding.maxDurationEdtView.text.toString().toInt(),
                    hasEquipment = binding.yesHasEquipmentRdGroup.isChecked
                )
                sharedViewModel.updatePreferences(updatedPreferences)
                Log.d("FormActivity2", "✅ Données sauvegardées: $updatedPreferences")
            }
        }
    }

    private fun navigateToFormActivity3(){
        Intent(this, FormActivity3::class.java).also {
            startActivity(it)
            finish()
        }
    }
}

