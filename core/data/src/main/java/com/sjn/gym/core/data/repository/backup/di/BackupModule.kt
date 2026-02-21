package com.sjn.gym.core.data.repository.backup.di

import com.sjn.gym.core.data.repository.backup.BackupRepository
import com.sjn.gym.core.data.repository.backup.impl.BackupRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BackupModule {
    @Binds
    @Singleton
    fun bindBackupRepository(impl: BackupRepositoryImpl): BackupRepository
}
