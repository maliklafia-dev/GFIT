package com.example.gfit.data.model.workout


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_programs")
data class WorkoutProgramEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val exercises: String, // JSON string des exercices
    val preferences: String, //JSON string des préférences
    val createdAt: Long = System.currentTimeMillis()
)