package com.sjn.gym.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.sjn.gym.core.data.repository.UserProfileRepository
import com.sjn.gym.core.data.repository.userProfileDataStore
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
    fun provideUserProfileRepository(
        @ApplicationContext context: Context,
    ): UserProfileRepository = UserProfileRepository(context)
}
