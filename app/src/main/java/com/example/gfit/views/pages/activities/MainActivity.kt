package com.example.gfit.views.pages.activities

import com.example.gfit.repositories.WorkoutRepository
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gfit.data.database.AppDatabase
import com.example.gfit.data.model.user.UserWorkoutPreferences
import com.example.gfit.data.network.service.RetrofitClient
import com.example.gfit.databinding.ActivityMainBinding
import com.example.gfit.repositories.UserRepository
import com.example.gfit.viewmodel.SharedViewModel
import com.example.gfit.viewmodel.SharedViewModelProvider
import com.example.gfit.viewmodel.UserViewModel
import com.example.gfit.viewmodel.WorkoutState
import com.example.gfit.viewmodel.WorkoutViewModel
import com.example.gfit.viewmodel.factory.UserViewModelFactory
import com.example.gfit.viewmodel.factory.WorkoutViewModelFactory
import com.example.gfit.views.adapters.WorkoutAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val workoutAdapter = WorkoutAdapter()

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            UserRepository(AppDatabase.getDatabase(this).userDao())
        )
    }

    private val sharedViewModel: SharedViewModel by lazy {
        SharedViewModelProvider.getSharedViewModel(this)
    }

    private val workoutViewModel: WorkoutViewModel by viewModels {
        WorkoutViewModelFactory(
            WorkoutRepository(
                RetrofitClient.workoutApiService,
                AppDatabase.getDatabase(this).workoutProgramDao()
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModels()
        setupRecyclerView()

        binding.logOutBtn.setOnClickListener {
            userViewModel.logout()
            navigateToLogin()
        }

        binding.getMyProgramTxtView.setOnClickListener {

            navigateToFormActivity1()
        }


        binding.fullNameTxtView.text = sharedViewModel.userPreferences.value?.name.toString()
        binding.userAgeTxtView.text = sharedViewModel.userPreferences.value?.age.toString()
        binding.userWeightTxtView.text = sharedViewModel.userPreferences.value?.height.toString()+" cm"
        binding.userWeightTxtView.text = sharedViewModel.userPreferences.value?.weight.toString()+" kg"
        binding.userGenderTxtView.text = sharedViewModel.userPreferences.value?.sex.toString()

        userViewModel.currentUser()?.uid?.let { userId ->
            workoutViewModel.loadSavedWorkoutProgram(userId)
            workoutViewModel.fetchUserInfos(userId)

            workoutViewModel.infos.observe(this) { userInfos ->
                binding.fullNameTxtView.text = userInfos?.name
                binding.userAgeTxtView.text = userInfos?.age.toString()
                binding.userHeightTxtView.text = userInfos?.height.toString()+" cm"
                binding.userWeightTxtView.text = userInfos?.weight.toString()+" kg"
                binding.userGenderTxtView.text = userInfos?.sex.toString()
            }
        }
    }

    private fun navigateToFormActivity1() {
        Intent(this, FormActivity1::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun navigateToLogin() {
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }


    private fun setupRecyclerView() {
        binding.workoutProgramRV.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = workoutAdapter
        }
    }

    private fun observeViewModels() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observer les préférences utilisateur
                launch {
                    sharedViewModel.userPreferences
                        .filterNotNull()
                        .collect { preferences ->
                            Log.d("MainActivity", "📥 Préférences reçues: $preferences")
                            if (isPreferencesComplete(preferences)) {
                                Log.d("MainActivity", "✅ Données complètes, génération du programme")
                                userViewModel.currentUser()?.uid?.let { userId ->
                                    workoutViewModel.generateWorkoutPlan(preferences, userId)
                                }
                            }
                        }
                }

                // Observer le plan d'entraînement
                launch {
                    workoutViewModel.workoutState.collect { state ->
                        when (state) {
                            is WorkoutState.Loading -> showLoading()
                            is WorkoutState.Success -> {
                                hideLoading()
                                workoutAdapter.submitList(state.exercises)
                            }
                            is WorkoutState.Error -> {
                                hideLoading()
                                showError(state.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isPreferencesComplete(preferences: UserWorkoutPreferences): Boolean {
        return with(preferences) {
            val isComplete = name.isNotBlank() &&
                    age > 0 &&
                    sex.isNotBlank() &&
                    height > 0 &&
                    weight > 0 &&
                    goal.isNotBlank() &&
                    workoutFrequency > 0 &&
                    maxSessionDuration > 0

            Log.d("MainActivity", "🔍 Vérification des données: $isComplete")
            if (!isComplete) {
                Log.d("MainActivity", """
                Champs manquants:
                name: ${name.isNotBlank()}
                age: $age
                sex: ${sex.isNotBlank()}
                height: $height
                weight: $weight
                goal: ${goal.isNotBlank()}
                workoutFrequency: $workoutFrequency
                maxSessionDuration: $maxSessionDuration
            """.trimIndent())
            }
            isComplete
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.workoutProgramRV.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.workoutProgramRV.visibility = View.VISIBLE
    }

    private fun showError(message: String) {

        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).setAction("Retry") {
            // Optionnel : permettre à l'utilisateur de réessayer
            sharedViewModel.userPreferences.value?.let {
                userViewModel.currentUser()?.uid?.let { userId ->
                    workoutViewModel.generateWorkoutPlan(it, userId)
                }
            }
        }.show()
    }
}