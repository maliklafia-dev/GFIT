package com.example.gfit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gfit.data.model.user.UserEntity
import com.example.gfit.data.model.workout.WorkoutProgramEntity
import com.example.gfit.data.network.dao.UserDao
import com.example.gfit.data.network.dao.WorkoutProgramDao

@Database(
    entities = [UserEntity::class, WorkoutProgramEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutProgramDao(): WorkoutProgramDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gfit_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}