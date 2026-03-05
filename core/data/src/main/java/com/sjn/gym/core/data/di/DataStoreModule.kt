package com.sjn.gym.core.data.di

import android.content.Context
import com.sjn.gym.core.data.repository.UserProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideUserProfileRepository(@ApplicationContext context: Context): UserProfileRepository =
        UserProfileRepository(context)
}
