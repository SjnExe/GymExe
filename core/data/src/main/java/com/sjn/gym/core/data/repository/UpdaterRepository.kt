package com.sjn.gym.core.data.repository

import android.os.Build
import com.sjn.gym.core.data.network.GitHubService
import javax.inject.Inject
import javax.inject.Singleton

data class UpdateInfo(
    val version: String,
    val releaseNotes: String,
    val downloadUrl: String,
    val isStable: Boolean,
)

@Singleton
class UpdaterRepository
    @Inject
    constructor(
        private val gitHubService: GitHubService,
    ) {
        suspend fun checkForUpdates(
            currentVersion: String,
            isDevBuild: Boolean,
        ): UpdateInfo? {
            return try {
                val releases = gitHubService.getReleases()
                if (releases.isEmpty()) return null

                // Logic:
                // If Dev Build: Look for latest release (likely a pre-release or latest nightly).
                // If Stable Build: Look for latest release that is NOT pre-release.

                val targetRelease =
                    if (isDevBuild) {
                        releases.firstOrNull() // Latest release (could be stable or pre-release)
                    } else {
                        releases.firstOrNull { !it.prerelease } // Latest stable
                    } ?: return null

                // Compare versions (Simple string comparison for now, ideally semantic versioning)
                // Assuming tag_name is like "v1.0.0" or "v1.0.0-dev..."
                if (targetRelease.tagName == currentVersion || targetRelease.tagName == "v$currentVersion") {
                    return null
                }

                // Find appropriate asset
                val architecture =
                    Build.SUPPORTED_ABIS.firstOrNull() ?: "universal" // e.g. arm64-v8a

                // Prioritize arch-specific APK, fallback to universal
                val asset =
                    targetRelease.assets.find {
                        it.name.contains(architecture, ignoreCase = true) && it.name.endsWith(".apk")
                    } ?: targetRelease.assets.find {
                        it.name.contains("universal", ignoreCase = true) && it.name.endsWith(".apk")
                    } ?: targetRelease.assets.find {
                        it.name.endsWith(".apk")
                    } ?: return null // No APK found

                UpdateInfo(
                    version = targetRelease.tagName,
                    releaseNotes = targetRelease.body,
                    downloadUrl = asset.downloadUrl,
                    isStable = !targetRelease.prerelease,
                )
            } catch (e: Exception) {
                null
            }
        }
    }
