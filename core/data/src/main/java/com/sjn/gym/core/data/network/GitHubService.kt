package com.sjn.gym.core.data.network

import retrofit2.http.GET

interface GitHubService {
    @GET("repos/SjnExe/GymExe/releases")
    suspend fun getReleases(): List<GitHubRelease>
}
