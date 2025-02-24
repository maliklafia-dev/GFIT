package com.example.gfit.views.pages.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gfit.databinding.ActivityForm44Binding

import com.example.gfit.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

class FormActivity4 : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityForm44Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForm44Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            saveForm4Data()
            navigateToAfterFormActivity()
        }
    }

    private fun navigateToAfterFormActivity() {
        Intent(this, MainActivity::class.java).also {
            // Ajout du flag pour effacer la pile d'activités précédentes
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    private fun saveForm4Data() {
        lifecycleScope.launch {
            sharedViewModel.userPreferences.value?.let { currentPrefs ->
                val updatedPreferences = currentPrefs.copy(
                    takesSupplements = binding.yesTakeSupplementsRdG.isChecked,
                    obstacles = binding.obstacleEdtView.text.toString(),
                    motivation = binding.motivationEdtView.text.toString()
                )
                Log.d("FormActivity4", "📤 Sauvegarde des dernières préférences: $updatedPreferences")
                sharedViewModel.updatePreferences(updatedPreferences)
                Log.d("FormActivity4", "${sharedViewModel.userPreferences}")
            }
        }
    }
}