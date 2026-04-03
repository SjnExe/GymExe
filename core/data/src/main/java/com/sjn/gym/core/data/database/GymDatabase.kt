package com.sjn.gym.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sjn.gym.core.data.database.converters.Converters
import com.sjn.gym.core.data.database.converters.LocalDateTimeConverter
import com.sjn.gym.core.data.database.dao.ExerciseDao
import com.sjn.gym.core.data.database.dao.RoutineDao
import com.sjn.gym.core.data.database.dao.WorkoutSessionDao
import com.sjn.gym.core.data.database.entity.ExerciseEntity
import com.sjn.gym.core.data.database.entity.RoutineEntity
import com.sjn.gym.core.data.database.entity.RoutineExerciseEntity
import com.sjn.gym.core.data.database.entity.WorkoutExerciseEntity
import com.sjn.gym.core.data.database.entity.WorkoutSessionEntity
import com.sjn.gym.core.data.database.entity.WorkoutSetEntity

@Database(
    entities =
        [
            ExerciseEntity::class,
            WorkoutSessionEntity::class,
            WorkoutExerciseEntity::class,
            WorkoutSetEntity::class,
            RoutineEntity::class,
            com.sjn.gym.core.data.database.entity.RoutineDayEntity::class,
            com.sjn.gym.core.data.database.entity.ExercisePreferencesEntity::class,
            RoutineExerciseEntity::class,
        ],
    version = 6,
    exportSchema = false,
)
@TypeConverters(Converters::class, LocalDateTimeConverter::class)
abstract class GymDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    abstract fun workoutSessionDao(): WorkoutSessionDao

    abstract fun routineDao(): RoutineDao

    abstract fun exercisePreferencesDao(): com.sjn.gym.core.data.database.dao.ExercisePreferencesDao

    companion object {
        val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "ALTER TABLE exercises ADD COLUMN instructions TEXT NOT NULL DEFAULT ''"
                    )
                }
            }

        val MIGRATION_2_3 =
            object : Migration(2, 3) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `workout_sessions` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT, PRIMARY KEY(`id`))"
                    )
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `workout_exercises` (`id` TEXT NOT NULL, `workoutSessionId` TEXT NOT NULL, `exerciseId` INTEGER NOT NULL, `orderIndex` INTEGER NOT NULL, `note` TEXT NOT NULL, PRIMARY KEY(`id`))"
                    )
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `workout_sets` (`id` TEXT NOT NULL, `workoutExerciseId` TEXT NOT NULL, `orderIndex` INTEGER NOT NULL, `type` TEXT NOT NULL, `weight` REAL, `reps` INTEGER, `timeSeconds` INTEGER, `distance` REAL, `rpe` INTEGER, `isCompleted` INTEGER NOT NULL, PRIMARY KEY(`id`))"
                    )
                }
            }

        val MIGRATION_3_4 =
            object : Migration(3, 4) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `routines` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, PRIMARY KEY(`id`))"
                    )
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `routine_exercises` (`id` TEXT NOT NULL, `routineId` TEXT NOT NULL, `exerciseId` INTEGER NOT NULL, `orderIndex` INTEGER NOT NULL, `targetSets` INTEGER NOT NULL, `targetReps` INTEGER, `restTimeSeconds` INTEGER, `note` TEXT NOT NULL, PRIMARY KEY(`id`))"
                    )
                }
            }
        val MIGRATION_4_5 =
            object : Migration(4, 5) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "ALTER TABLE exercises ADD COLUMN muscleAffectionSeverity INTEGER NOT NULL DEFAULT 0"
                    )
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `exercise_preferences` (`exerciseId` TEXT NOT NULL, `defaultRestTimeSeconds` INTEGER NOT NULL, `defaultSets` INTEGER NOT NULL, `defaultReps` INTEGER NOT NULL, `defaultWeight` REAL NOT NULL, PRIMARY KEY(`exerciseId`))"
                    )
                }
            }
        val MIGRATION_5_6 =
            object : Migration(5, 6) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        "ALTER TABLE routines ADD COLUMN routineType TEXT NOT NULL DEFAULT 'REPEAT'"
                    )
                    db.execSQL(
                        "ALTER TABLE routine_exercises ADD COLUMN routineDayId TEXT NOT NULL DEFAULT ''"
                    )
                    db.execSQL(
                        "ALTER TABLE routine_exercises ADD COLUMN setType TEXT NOT NULL DEFAULT 'NORMAL'"
                    )
                    db.execSQL(
                        "CREATE TABLE IF NOT EXISTS `routine_days` (`id` TEXT NOT NULL, `routineId` TEXT NOT NULL, `dayName` TEXT NOT NULL, `dayIndex` INTEGER NOT NULL, `isRestDay` INTEGER NOT NULL, `universalRestTimeSeconds` INTEGER NOT NULL, PRIMARY KEY(`id`))"
                    )
                }
            }
    }
}
