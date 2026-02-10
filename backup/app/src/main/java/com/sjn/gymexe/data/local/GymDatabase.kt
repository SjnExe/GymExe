package com.sjn.gymexe.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sjn.gymexe.data.local.dao.ExerciseDao
import com.sjn.gymexe.data.local.dao.ProgramDao
import com.sjn.gymexe.data.local.dao.SessionDao
import com.sjn.gymexe.data.local.dao.SetDao
import com.sjn.gymexe.data.local.entity.ExerciseEntity
import com.sjn.gymexe.data.local.entity.ProgramEntity
import com.sjn.gymexe.data.local.entity.SessionEntity
import com.sjn.gymexe.data.local.entity.SetEntity
import com.sjn.gymexe.data.local.entity.WorkoutEntity
import com.sjn.gymexe.data.local.entity.WorkoutExerciseEntity

@Database(
    entities = [
        ExerciseEntity::class,
        ProgramEntity::class,
        WorkoutEntity::class,
        WorkoutExerciseEntity::class,
        SessionEntity::class,
        SetEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class GymDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    abstract fun programDao(): ProgramDao

    abstract fun sessionDao(): SessionDao

    abstract fun setDao(): SetDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Update sets table
                db.execSQL("ALTER TABLE sets ADD COLUMN duration INTEGER DEFAULT NULL")
                db.execSQL("ALTER TABLE sets ADD COLUMN distance REAL DEFAULT NULL")
                db.execSQL("ALTER TABLE sets ADD COLUMN inputString TEXT DEFAULT NULL")

                // Update exercises table
                db.execSQL("ALTER TABLE exercises ADD COLUMN equipmentCategory TEXT NOT NULL DEFAULT 'BARBELL'")
                db.execSQL("ALTER TABLE exercises ADD COLUMN machineIncrement REAL DEFAULT NULL")
                db.execSQL("ALTER TABLE exercises ADD COLUMN machineMax REAL DEFAULT NULL")
            }
        }
    }
}
