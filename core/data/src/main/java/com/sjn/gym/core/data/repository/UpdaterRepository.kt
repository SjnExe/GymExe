package com.sjn.gym.core.data.repository

import android.os.Build
import com.sjn.gym.core.data.network.GitHubService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

data class UpdateInfo(
    val version: String,
    val releaseNotes: String,
    val downloadUrl: String,
    val isStable: Boolean,
    val architecture: String? = null,
)

sealed class DownloadStatus {
    object Idle : DownloadStatus()

    data class Downloading(
        val progress: Int,
    ) : DownloadStatus()

    data class Success(
        val file: File,
    ) : DownloadStatus()

    data class Error(
        val message: String,
    ) : DownloadStatus()
}

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

                if (isDevBuild) {
                    // Dev Build Logic: Check 'dev' tag
                    val devRelease = releases.find { it.tagName == "dev" } ?: return null

                    // Parse version from body
                    // Expected format: "Version: 1.2.3-dev-..."
                    val versionRegex = "Version: (.*)".toRegex()
                    val matchResult = versionRegex.find(devRelease.body)
                    val remoteVersion = matchResult?.groupValues?.get(1)?.trim() ?: return null

                    if (remoteVersion == currentVersion) return null

                    // Find APK (Universal only for dev, avoid debug apk)
                    // User said: "it shouldn't download the dev debug apk"
                    // Assets: app-dev-release.apk, app-dev-debug.apk
                    val asset =
                        devRelease.assets.find {
                            it.name.endsWith(".apk") &&
                                !it.name.contains("debug", ignoreCase = true)
                        } ?: return null

                    UpdateInfo(
                        version = remoteVersion,
                        releaseNotes = devRelease.body,
                        downloadUrl = asset.downloadUrl,
                        isStable = false,
                        architecture = "universal",
                    )
                } else {
                    // Stable Build Logic: Check latest stable release
                    val stableRelease = releases.firstOrNull { !it.prerelease } ?: return null

                    // Compare versions
                    val remoteVersion = stableRelease.tagName.removePrefix("v")
                    if (remoteVersion == currentVersion) return null

                    // Find APK: Architecture specific > Universal > Any
                    val supportedAbis = Build.SUPPORTED_ABIS
                    var selectedAsset: com.sjn.gym.core.data.network.GitHubAsset? = null
                    var arch: String? = null

                    for (abi in supportedAbis) {
                        selectedAsset =
                            stableRelease.assets.find {
                                it.name.contains(abi, ignoreCase = true) && it.name.endsWith(".apk")
                            }
                        if (selectedAsset != null) {
                            arch = abi
                            break
                        }
                    }

                    if (selectedAsset == null) {
                        selectedAsset =
                            stableRelease.assets.find {
                                it.name.contains("universal", ignoreCase = true) &&
                                    it.name.endsWith(".apk")
                            }
                        if (selectedAsset != null) arch = "universal"
                    }

                    if (selectedAsset == null) {
                        // Fallback to main APK if no specific arch or universal found
                        selectedAsset = stableRelease.assets.find { it.name.endsWith(".apk") }
                    }

                    if (selectedAsset == null) return null

                    UpdateInfo(
                        version = stableRelease.tagName, // Keep v-prefix if preferred, or remove it. Keeping tag name is safer.
                        releaseNotes = stableRelease.body,
                        downloadUrl = selectedAsset.downloadUrl,
                        isStable = true,
                        architecture = arch ?: "universal",
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun downloadApk(
            url: String,
            destinationFile: File,
        ): Flow<DownloadStatus> =
            flow {
                emit(DownloadStatus.Downloading(0))
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.connect()

                    if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                        emit(DownloadStatus.Error("Server returned ${connection.responseCode}"))
                        return@flow
                    }

                    val fileLength = connection.contentLength
                    val input = connection.inputStream
                    val output = FileOutputStream(destinationFile)

                    val data = ByteArray(4096)
                    var total: Long = 0
                    var count: Int

                    while (input.read(data).also { count = it } != -1) {
                        total += count
                        if (fileLength > 0) {
                            val progress = (total * 100 / fileLength).toInt()
                            emit(DownloadStatus.Downloading(progress))
                        }
                        output.write(data, 0, count)
                    }

                    output.flush()
                    output.close()
                    input.close()
                    emit(DownloadStatus.Success(destinationFile))
                } catch (e: Exception) {
                    emit(DownloadStatus.Error(e.message ?: "Download failed"))
                }
            }.flowOn(Dispatchers.IO)
    }
