package com.example.gfit.data.database

import androidx.room.TypeConverter
import com.example.gfit.data.model.user.UserWorkoutPreferences
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromUserWorkoutPreferences(preferences: UserWorkoutPreferences): String {
        return gson.toJson(preferences)
    }

    @TypeConverter
    fun toUserWorkoutPreferences(json: String): UserWorkoutPreferences {
        val type = object : TypeToken<UserWorkoutPreferences>() {}.type
        return gson.fromJson(json, type)
    }
}