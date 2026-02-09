package com.sjn.gymexe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sjn.gymexe.data.local.entity.SessionEntity
import com.sjn.gymexe.data.local.entity.SetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>

    @Query("SELECT * FROM sessions")
    @JvmSuppressWildcards
    suspend fun getAllSessionsList(): List<SessionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertSession(session: SessionEntity): Long
}

@Dao
interface SetDao {
    @Query("SELECT * FROM sets WHERE sessionId = :sessionId")
    fun getSetsForSession(sessionId: Long): Flow<List<SetEntity>>

    @Query("SELECT * FROM sets")
    @JvmSuppressWildcards
    suspend fun getAllSetsList(): List<SetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertSets(sets: List<SetEntity>): List<Long>

    // Smart Prefill Queries

    @Query("""
        SELECT * FROM sets
        WHERE exerciseId = :exerciseId
        ORDER BY timestamp DESC
        LIMIT 1
    """)
    @JvmSuppressWildcards
    suspend fun getLastSetForExercise(exerciseId: String): SetEntity?

    @Query("""
        SELECT MAX(weight) FROM sets
        WHERE exerciseId = :exerciseId
    """)
    @JvmSuppressWildcards
    suspend fun getPersonalRecordForExercise(exerciseId: String): Float?
}
