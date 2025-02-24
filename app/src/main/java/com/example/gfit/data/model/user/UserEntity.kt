package com.example.gfit.data.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val email: String,
    val uid: String,
    val lastLoginTime: Long = System.currentTimeMillis()
)