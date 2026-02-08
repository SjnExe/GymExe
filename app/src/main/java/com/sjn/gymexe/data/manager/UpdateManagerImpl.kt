package com.sjn.gymexe.data.manager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.sjn.gymexe.BuildConfig
import com.sjn.gymexe.domain.manager.DownloadStatus
import com.sjn.gymexe.domain.manager.UpdateManager
import com.sjn.gymexe.domain.manager.UpdateResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

private const val BUFFER_SIZE = 8192
private const val RELEASE_LAG_TOLERANCE_MS = 20 * 60 * 1000L // 20 Minutes

class UpdateManagerImpl(
    private val context: Context,
    private val json: Json,
) : UpdateManager {
    private val repo = "SjnExe/GymExe"

    // flavor is "dev" or "prod"
    private val isBeta = BuildConfig.FLAVOR == "dev"

    @Suppress("TooGenericExceptionCaught")
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

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
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

                        if (publishedAt > buildTime + RELEASE_LAG_TOLERANCE_MS) {
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
                return@withContext UpdateResult.Error(e)
            }
        }

    private fun findCorrectAsset(
        json: JsonObject,
        isBeta: Boolean,
    ): UpdateResult {
        val assets = json["assets"]?.jsonArray ?: return UpdateResult.Error(Exception("No assets found"))

        val targetUrl = findBestUrl(assets)

        return if (targetUrl != null) {
            val version = json["tag_name"]?.jsonPrimitive?.content ?: "Unknown"
            UpdateResult.UpdateAvailable(version, targetUrl, isBeta)
        } else {
            UpdateResult.Error(Exception("No suitable APK found"))
        }
    }

    private fun findBestUrl(
        assets: kotlinx.serialization.json.JsonArray,
    ): String? {
        val entries = assets.asSequence().mapNotNull { getNameAndUrl(it) }
        val abis = Build.SUPPORTED_ABIS.toList()

        val searchKeys = sequenceOf(
            abis.firstOrNull(),
            "arm64-v8a".takeIf { abis.contains(it) },
            "armeabi-v7a".takeIf { abis.contains(it) },
            "x86_64".takeIf { abis.contains(it) },
            "Universal"
        ).filterNotNull()

        return searchKeys
            .mapNotNull { key -> entries.find { it.first.contains(key, ignoreCase = true) }?.second }
            .firstOrNull() ?: entries.find { it.first.endsWith(".apk", ignoreCase = true) }?.second
    }

    private fun getNameAndUrl(asset: kotlinx.serialization.json.JsonElement): Pair<String, String>? {
        val obj = asset.jsonObject
        val name = obj["name"]?.jsonPrimitive?.content
        val url = obj["browser_download_url"]?.jsonPrimitive?.content
        return if (name != null && url != null) name to url else null
    }

    @Suppress("TooGenericExceptionCaught")
    override fun downloadUpdate(
        url: String,
        fileName: String,
    ) {
        try {
            val request =
                DownloadManager
                    .Request(Uri.parse(url))
                    .setTitle("GymExe Update")
                    .setDescription("Downloading version...")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    .setMimeType("application/vnd.android.package-archive")

            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        } catch (e: Exception) {
            Log.e("UpdateManager", "Download failed", e)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override fun downloadUpdateFlow(
        url: String,
        fileName: String,
    ): Flow<DownloadStatus> = flow {
        emit(DownloadStatus.Downloading(0f))
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                emit(DownloadStatus.Error("Server returned ${connection.responseCode}"))
                return@flow
            }

            val length = connection.contentLength
            val input = connection.inputStream

            // Use internal files dir for provider compatibility
            val updateDir = File(context.filesDir, "updates")
            if (!updateDir.exists()) updateDir.mkdirs()
            val file = File(updateDir, fileName)

            val output = FileOutputStream(file)
            val buffer = ByteArray(BUFFER_SIZE)
            var bytesRead: Int
            var totalBytesRead = 0L

            while (input.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                if (length > 0) {
                    val progress = totalBytesRead.toFloat() / length
                    emit(DownloadStatus.Downloading(progress))
                }
            }
            output.close()
            input.close()

            val authority = "${context.packageName}.provider"
            val uri = FileProvider.getUriForFile(context, authority, file)

            emit(DownloadStatus.Completed(uri.toString()))
        } catch (e: Exception) {
            Log.e("UpdateManager", "Download failed", e)
            emit(DownloadStatus.Error(e.message ?: "Download failed"))
        }
    }.flowOn(Dispatchers.IO)
}
