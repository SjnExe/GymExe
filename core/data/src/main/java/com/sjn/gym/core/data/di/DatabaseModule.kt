package com.sjn.gym.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sjn.gym.core.data.database.GymDatabase
import com.sjn.gym.core.data.database.dao.ExerciseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): GymDatabase {
        return Room.databaseBuilder(
            context,
            GymDatabase::class.java,
            "gym_database.db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO exercises (name, bodyPart, targetMuscle, secondaryMuscles, equipment, type, isCustom) VALUES ('Bench Press', 'Chest', 'Pectoralis Major', 'Triceps,Anterior Deltoid', 'Barbell', 'WEIGHT_REPS', 0)")
                db.execSQL("INSERT INTO exercises (name, bodyPart, targetMuscle, secondaryMuscles, equipment, type, isCustom) VALUES ('Squat', 'Legs', 'Quadriceps', 'Glutes,Hamstrings', 'Barbell', 'WEIGHT_REPS', 0)")
                db.execSQL("INSERT INTO exercises (name, bodyPart, targetMuscle, secondaryMuscles, equipment, type, isCustom) VALUES ('Deadlift', 'Back', 'Erector Spinae', 'Glutes,Hamstrings,Traps', 'Barbell', 'WEIGHT_REPS', 0)")
            }
        }).build()
    }

    @Provides
    fun provideExerciseDao(db: GymDatabase): ExerciseDao = db.exerciseDao()
}
