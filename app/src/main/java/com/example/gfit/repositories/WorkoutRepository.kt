package com.example.gfit.repositories
import android.util.Log
import com.example.gfit.data.model.user.UserWorkoutPreferences
import com.example.gfit.data.model.workout.WorkoutExercise
import com.example.gfit.data.network.dto.workout_dto.Part
import com.example.gfit.data.network.dto.workout_dto.RequestContent
import com.example.gfit.data.network.dto.workout_dto.WorkoutRequest
import com.example.gfit.data.network.mapper.WorkoutMapper
import com.example.gfit.data.network.service.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkoutRepository {
    private val apiService = RetrofitClient.workoutApiService
    private val apiKey = RetrofitClient.getApiKey()

    suspend fun fetchWorkoutPlan(userPreferences: UserWorkoutPreferences): List<WorkoutExercise> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.generateWorkout(
                    WorkoutRequest(
                        contents = listOf(
                            RequestContent(
                                parts = listOf(Part(text = buildPrompt(userPreferences)))
                            )
                        )
                    ),
                    apiKey
                )

                if (response.isSuccessful && response.body() != null) {
                    val content = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                        ?: throw Exception("Pas de contenu généré")

                    Log.d("com.example.gfit.repositories.WorkoutRepository", "📄 Contenu brut reçu: $content")

                    val exercises = WorkoutMapper.fromApiResponse(content)
                    Log.d("com.example.gfit.repositories.WorkoutRepository", "✅ Programme généré: ${exercises.size} exercices")
                    exercises
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("com.example.gfit.repositories.WorkoutRepository", "❌ Erreur API: $errorBody")
                    throw Exception("Erreur API: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                Log.e("com.example.gfit.repositories.WorkoutRepository", "❌ Exception: ${e.message}", e)
                throw e
            }
        }

    private fun buildPrompt(preferences: UserWorkoutPreferences): String {
        return """
            Generate a workout plan in valid JSON array format. Each exercise must follow this exact structure:
            [
              {
                "title": "Exercise name",
                "duration": minutes (integer),
                "day": "Week X, Day Y",
                "imageUrl": "imageUrl" 
              }
            ]
            Use these real image URLs for exercises (replace imageUrl with one of these):
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740345813/bench_gcblxz.jpg"
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740345814/squat_lgovdp.jpg"
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740345813/deadlift_ir8x4i.jpg"
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740359059/push_ups_bqgggs.jpg"
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740359059/shoulder_press_bjkhxt.jpg"
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740359060/leg_press_uyjirp.jpg"
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740359187/cardio_uiarh0.jpg"
            - "https://res.cloudinary.com/dk8kwgodg/image/upload/v1740359188/elpitic_bicyclepng_imm9tq.png"
            
            
            User profile:
            {
                "name": "${preferences.name}",
                "age": ${preferences.age},
                "sex": "${preferences.sex}",
                "weight": ${preferences.weight},
                "height": ${preferences.height},
                "goal": "${preferences.goal}",
                "frequency": ${preferences.workoutFrequency},
                "max_duration": ${preferences.maxSessionDuration},
                "equipment": ${preferences.hasEquipment},
                "activity_level": "${preferences.activityLevel}"
            }
            
            Generate a personalized plan based on this profile.
            Return ONLY the JSON array, without markdown formatting or additional text.
        """.trimIndent()
    }

}