package com.example.gfit.data.network.service

import com.example.gfit.data.network.dto.workout_dto.WorkoutRequest
import com.example.gfit.data.network.dto.workout_dto.WorkoutResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface WorkoutApiService {
    @Headers("Content-Type: application/json")
    @POST("models/gemini-1.5-flash:generateContent")
    suspend fun generateWorkout(
        @Body request: WorkoutRequest,
        @Query("key") apiKey: String
    ): Response<WorkoutResponse>
}
