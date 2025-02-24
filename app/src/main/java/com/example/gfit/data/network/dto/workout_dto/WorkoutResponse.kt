package com.example.gfit.data.network.dto.workout_dto

// WorkoutResponse.kt
data class WorkoutResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)