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
    fun provideDatabase(@ApplicationContext context: Context): GymDatabase =
        Room.databaseBuilder(context, GymDatabase::class.java, "gym_database.db")
            .addMigrations(
                GymDatabase.MIGRATION_1_2,
                GymDatabase.MIGRATION_2_3,
                GymDatabase.MIGRATION_3_4,
            )
            .fallbackToDestructiveMigration(false) // Data persistence required.
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed data
                        val exercises =
                            listOf(
                                "('Bench Press', 'Chest', 'Pectoralis Major', 'Triceps,Anterior Deltoid', 'Barbell', 'WEIGHT_REPS', 0, 'Lie flat on your back on a bench. Grasp the barbell with an overhand grip, slightly wider than shoulder-width apart. Lower the bar to your mid-chest, then press it back up to the starting position.')",
                                "('Squat', 'Legs', 'Quadriceps', 'Glutes,Hamstrings', 'Barbell', 'WEIGHT_REPS', 0, 'Stand with feet shoulder-width apart. Rest the barbell across your upper back. Bend your knees and hips to lower your body as if sitting in a chair, keeping your chest up and back straight. Push through your heels to return to the starting position.')",
                                "('Deadlift', 'Back', 'Erector Spinae', 'Glutes,Hamstrings,Traps', 'Barbell', 'WEIGHT_REPS', 0, 'Stand with feet hip-width apart, barbell over your mid-foot. Bend at the hips and knees to grip the bar. Keeping your back flat, lift the bar by extending your hips and knees until you are standing fully upright.')",
                                "('Overhead Press', 'Shoulders', 'Anterior Deltoid', 'Triceps,Lateral Deltoid', 'Barbell', 'WEIGHT_REPS', 0, 'Stand with feet shoulder-width apart. Hold the barbell at shoulder level with an overhand grip. Press the bar straight up overhead until your arms are fully extended, then lower it back to the starting position.')",
                                "('Pull Up', 'Back', 'Latissimus Dorsi', 'Biceps,Rear Deltoid', 'Bodyweight', 'REPS_ONLY', 0, 'Grasp a pull-up bar with an overhand grip slightly wider than shoulder-width apart. Hang freely, then pull yourself up until your chin clears the bar. Lower yourself back down with control.')",
                                "('Dumbbell Row', 'Back', 'Latissimus Dorsi', 'Biceps,Rear Deltoid', 'Dumbbell', 'WEIGHT_REPS', 0, 'Place one knee and hand on a bench. Hold a dumbbell in the other hand. Pull the dumbbell up to your side, keeping your elbow close to your body. Lower the weight under control.')",
                                "('Lunges', 'Legs', 'Quadriceps', 'Glutes,Hamstrings', 'Dumbbell', 'WEIGHT_REPS', 0, 'Hold a dumbbell in each hand. Step forward with one leg and lower your hips until both knees are bent at a 90-degree angle. Push back to the starting position and repeat on the other side.')",
                                "('Bicep Curl', 'Arms', 'Biceps Brachii', 'Brachialis,Brachioradialis', 'Dumbbell', 'WEIGHT_REPS', 0, 'Stand tall holding a dumbbell in each hand, arms fully extended and palms facing forward. Curl the weights upward towards your shoulders, contracting your biceps. Lower back down with control.')",
                                "('Tricep Extension', 'Arms', 'Triceps Brachii', '', 'Dumbbell', 'WEIGHT_REPS', 0, 'Hold a single dumbbell with both hands behind your head, elbows pointing up. Extend your arms to raise the dumbbell, then slowly lower it back to the starting position.')",
                                "('Leg Press', 'Legs', 'Quadriceps', 'Glutes,Hamstrings,Calves', 'Machine', 'WEIGHT_REPS', 0, 'Sit in the leg press machine and place your feet on the platform shoulder-width apart. Lower the platform by bending your knees to 90 degrees, then push it back up.')",
                                "('Lat Pulldown', 'Back', 'Latissimus Dorsi', 'Biceps,Rear Deltoid', 'Machine', 'WEIGHT_REPS', 0, 'Sit at a lat pulldown machine and grasp the wide bar with an overhand grip. Pull the bar down to your upper chest, squeezing your shoulder blades together. Return the bar with control.')",
                                "('Plank', 'Core', 'Rectus Abdominis', 'Obliques,Transverse Abdominis', 'Bodyweight', 'TIME', 0, 'Start in a push-up position, but rest your weight on your forearms instead of your hands. Keep your body in a straight line from head to heels and brace your core. Hold this position.')",
                                "('Crunch', 'Core', 'Rectus Abdominis', 'Obliques', 'Bodyweight', 'REPS_ONLY', 0, 'Lie on your back with knees bent and feet flat on the floor. Place your hands lightly behind your head. Contract your abs to lift your shoulder blades off the floor, then slowly lower back down.')",
                            )

                        exercises.forEach { values ->
                            db.execSQL(
                                "INSERT INTO exercises (name, bodyPart, targetMuscle, secondaryMuscles, equipment, type, isCustom, instructions) VALUES $values"
                            )
                        }
                    }
                }
            )
            .build()

    @Provides fun provideExerciseDao(db: GymDatabase): ExerciseDao = db.exerciseDao()

    @Provides fun provideRoutineDao(db: GymDatabase) = db.routineDao()
}
