package com.sjn.gymexe.data.repository

import com.sjn.gymexe.data.local.dao.ProgramDao
import com.sjn.gymexe.data.local.entity.ProgramEntity
import com.sjn.gymexe.data.local.relation.ProgramWithWorkouts
import com.sjn.gymexe.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow

class ProgramRepositoryImpl(
    private val programDao: ProgramDao,
) : ProgramRepository {
    override fun getAllPrograms(): Flow<List<ProgramEntity>> = programDao.getAllPrograms()

    override fun getActiveProgram(): Flow<ProgramEntity?> = programDao.getActiveProgram()

    override fun getProgramWithWorkouts(programId: Long): Flow<ProgramWithWorkouts> =
        programDao.getProgramWithWorkouts(programId)

    override suspend fun createProgram(program: ProgramEntity): Long {
        return programDao.insertProgram(program)
    }

    override suspend fun setActiveProgram(programId: Long) {
        programDao.clearActivePrograms()
        programDao.setActiveProgram(programId)
    }

    override suspend fun advanceRollingSplit(programId: Long) {
        val program = programDao.getProgramById(programId)
        if (program != null && program.type == "Rolling") {
            val updated = program.copy(currentSplitIndex = program.currentSplitIndex + 1)
            programDao.insertProgram(updated)
        }
    }
}
