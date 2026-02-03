package com.sjn.gymexe.domain.repository

import com.sjn.gymexe.data.local.entity.ProgramEntity
import com.sjn.gymexe.data.local.relation.ProgramWithWorkouts
import kotlinx.coroutines.flow.Flow

interface ProgramRepository {
    fun getAllPrograms(): Flow<List<ProgramEntity>>

    fun getActiveProgram(): Flow<ProgramEntity?>

    fun getProgramWithWorkouts(programId: Long): Flow<ProgramWithWorkouts>

    suspend fun createProgram(program: ProgramEntity): Long

    suspend fun setActiveProgram(programId: Long)

    // Logic
    suspend fun advanceRollingSplit(programId: Long)
}
