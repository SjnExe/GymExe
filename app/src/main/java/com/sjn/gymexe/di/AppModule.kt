package com.sjn.gymexe.di

import android.content.Context
import androidx.room.Room
import com.sjn.gymexe.data.local.GymDatabase
import com.sjn.gymexe.data.local.dao.ExerciseDao
import com.sjn.gymexe.data.local.dao.ProgramDao
import com.sjn.gymexe.data.manager.BackupManagerImpl
import com.sjn.gymexe.data.manager.TimerManagerImpl
import com.sjn.gymexe.data.manager.UpdateManagerImpl
import com.sjn.gymexe.data.repository.ExerciseRepositoryImpl
import com.sjn.gymexe.data.repository.ProgramRepositoryImpl
import com.sjn.gymexe.domain.manager.BackupManager
import com.sjn.gymexe.domain.manager.TimerManager
import com.sjn.gymexe.domain.manager.UpdateManager
import com.sjn.gymexe.domain.repository.ExerciseRepository
import com.sjn.gymexe.domain.repository.ProgramRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }

    @Provides
    @Singleton
    fun provideGymDatabase(
        @ApplicationContext context: Context,
    ): GymDatabase {
        return Room.databaseBuilder(
            context,
            GymDatabase::class.java,
            "gym_exe.db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideExerciseDao(db: GymDatabase): ExerciseDao {
        return db.exerciseDao()
    }

    @Provides
    @Singleton
    fun provideProgramDao(db: GymDatabase): ProgramDao {
        return db.programDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(db: GymDatabase): com.sjn.gymexe.data.local.dao.SessionDao {
        return db.sessionDao()
    }

    @Provides
    @Singleton
    fun provideSetDao(db: GymDatabase): com.sjn.gymexe.data.local.dao.SetDao {
        return db.setDao()
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(
        @ApplicationContext context: Context,
        exerciseDao: ExerciseDao,
        json: Json,
    ): ExerciseRepository {
        return ExerciseRepositoryImpl(context, exerciseDao, json)
    }

    @Provides
    @Singleton
    fun provideBackupManager(
        @ApplicationContext context: Context,
        db: GymDatabase,
        json: Json,
    ): BackupManager {
        return BackupManagerImpl(context, db, json)
    }

    @Provides
    @Singleton
    fun provideUpdateManager(
        @ApplicationContext context: Context,
        json: Json,
    ): UpdateManager {
        return UpdateManagerImpl(context, json)
    }

    @Provides
    @Singleton
    fun provideProgramRepository(programDao: ProgramDao): ProgramRepository {
        return ProgramRepositoryImpl(programDao)
    }

    @Provides
    @Singleton
    fun provideTimerManager(): TimerManager {
        return TimerManagerImpl()
    }
}
