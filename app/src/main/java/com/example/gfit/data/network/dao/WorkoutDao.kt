package com.example.gfit.data.network.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gfit.data.model.workout.WorkoutProgramEntity

@Dao
interface WorkoutProgramDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutProgram(program: WorkoutProgramEntity)

    @Query("SELECT * FROM workout_programs WHERE userId = :userId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestWorkoutProgram(userId: String): WorkoutProgramEntity?

    @Query("DELETE FROM workout_programs WHERE userId = :userId")
    suspend fun deleteUserWorkoutPrograms(userId: String)
}