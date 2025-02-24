package com.example.gfit.data.database

import androidx.room.TypeConverter
import com.example.gfit.data.model.workout.WorkoutExercise
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromExerciseList(exercises: List<WorkoutExercise>): String {
        return Gson().toJson(exercises)
    }

    @TypeConverter
    fun toExerciseList(exercisesJson: String): List<WorkoutExercise> {
        val type = object : TypeToken<List<WorkoutExercise>>() {}.type
        return Gson().fromJson(exercisesJson, type)
    }
}