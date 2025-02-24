package com.example.gfit.data.network.dto.workout_dto

data class WorkoutRequest(
    val contents: List<RequestContent>
)

data class RequestContent(
    val parts: List<Part>
)