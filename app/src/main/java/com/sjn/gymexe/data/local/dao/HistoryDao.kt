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
    suspend fun getAllSessionsList(): List<SessionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity): Long
}

@Dao
interface SetDao {
    @Query("SELECT * FROM sets WHERE sessionId = :sessionId")
    fun getSetsForSession(sessionId: Long): Flow<List<SetEntity>>

    @Query("SELECT * FROM sets")
    suspend fun getAllSetsList(): List<SetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<SetEntity>)
}
