package com.gym.exe.data.manager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.gym.exe.BuildConfig
import com.gym.exe.domain.manager.UpdateManager
import com.gym.exe.domain.manager.UpdateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

class UpdateManagerImpl(
    private val context: Context,
    private val json: Json,
) : UpdateManager {
    private val repo = "SjnExe/GymExe"

    // flavor is "dev" or "prod"
    private val isBeta = BuildConfig.FLAVOR == "dev"

    override suspend fun checkForUpdates(): UpdateResult =
        withContext(Dispatchers.IO) {
            try {
                val urlStr =
                    if (isBeta) {
                        "https://api.github.com/repos/$repo/releases/tags/beta"
                    } else {
                        "https://api.github.com/repos/$repo/releases/latest"
                    }

                val url = URL(urlStr)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json")

                if (connection.responseCode != 200) {
                    // If 404, maybe no release yet
                    return@withContext UpdateResult.NoUpdate
                }

                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val releaseJson = json.parseToJsonElement(response).jsonObject

                if (isBeta) {
                    // Check Timestamp
                    val publishedAtStr = releaseJson["published_at"]?.jsonPrimitive?.content
                    if (publishedAtStr != null) {
                        val publishedAt = Instant.parse(publishedAtStr).toEpochMilli()
                        val buildTime = BuildConfig.BUILD_TIME

                        if (publishedAt > buildTime) {
                            return@withContext findCorrectAsset(releaseJson, true)
                        }
                    }
                } else {
                    // Check Version Name
                    val tagName = releaseJson["tag_name"]?.jsonPrimitive?.content ?: ""
                    val currentVersion = "v${BuildConfig.VERSION_NAME}"
                    // Simple string compare.
                    // If tag != current, assume update.
                    if (tagName != currentVersion && tagName.isNotEmpty()) {
                        return@withContext findCorrectAsset(releaseJson, false)
                    }
                }

                return@withContext UpdateResult.NoUpdate
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext UpdateResult.Error(e)
            }
        }

    private fun findCorrectAsset(
        json: JsonObject,
        isBeta: Boolean,
    ): UpdateResult {
        val assets = json["assets"]?.jsonArray ?: return UpdateResult.Error(Exception("No assets found"))
        val supportedAbis = Build.SUPPORTED_ABIS

        val isArm64 = supportedAbis.contains("arm64-v8a")

        var targetUrl: String? = null

        for (asset in assets) {
            val assetObj = asset.jsonObject
            val name = assetObj["name"]?.jsonPrimitive?.content ?: continue
            val downloadUrl = assetObj["browser_download_url"]?.jsonPrimitive?.content ?: continue

            if (isArm64 && name.contains("ARM64", ignoreCase = true)) {
                targetUrl = downloadUrl
                break
            }
            if (name.contains("Universal", ignoreCase = true)) {
                if (targetUrl == null) targetUrl = downloadUrl
            }
        }

        if (targetUrl == null) {
            for (asset in assets) {
                val assetObj = asset.jsonObject
                val name = assetObj["name"]?.jsonPrimitive?.content ?: continue
                if (name.endsWith(".apk")) {
                    targetUrl = assetObj["browser_download_url"]?.jsonPrimitive?.content
                    break
                }
            }
        }

        return if (targetUrl != null) {
            val version = json["tag_name"]?.jsonPrimitive?.content ?: "Unknown"
            UpdateResult.UpdateAvailable(version, targetUrl, isBeta)
        } else {
            UpdateResult.Error(Exception("No suitable APK found"))
        }
    }

    override fun downloadUpdate(
        url: String,
        fileName: String,
    ) {
        try {
            val request =
                DownloadManager.Request(Uri.parse(url))
                    .setTitle("GymExe Update")
                    .setDescription("Downloading version...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    .setMimeType("application/vnd.android.package-archive")

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
