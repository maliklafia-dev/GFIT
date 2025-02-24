package com.example.gfit.data.model.user

data class UserWorkoutPreferences(
    val name: String,
    val age: Int,
    val sex: String,
    val height: Int,
    val weight: Int,
    val targetWeight: Int?,
    val goal: String,
    val priority: String,
    val workoutFrequency: Int,
    val maxSessionDuration: Int,
    val hasEquipment: Boolean,
    val activityLevel: String,
    val sportsPracticed: String?,
    val diet: String,
    val allergies: String?,
    val cookingLevel: String,
    val takesSupplements: Boolean,
    val obstacles: String,
    val motivation: String
)
