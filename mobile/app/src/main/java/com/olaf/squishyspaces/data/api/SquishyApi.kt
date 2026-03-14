package com.olaf.squishyspaces.data.api

import android.content.ContentResolver
import android.net.Uri
import com.olaf.squishyspaces.data.model.RoomAnalysis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:3000"

private val client = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

private val json = Json { ignoreUnknownKeys = true }

object SquishyApi {
    suspend fun analyzeRoom(
        uri: Uri,
        contentResolver: ContentResolver,
        squidMode: String = "honest",
    ): RoomAnalysis =
        withContext(Dispatchers.IO) {
            val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
            val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: error("Could not read image from URI")

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    name = "file",
                    filename = "room.jpg",
                    body = bytes.toRequestBody(mimeType.toMediaType()),
                )
                .addFormDataPart("squidMode", squidMode)
                .build()

            val request = Request.Builder()
                .url("$BASE_URL/analyze-room")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: error("Empty response body")

            if (!response.isSuccessful) {
                error("Server error ${response.code}: $body")
            }

            json.decodeFromString<RoomAnalysis>(body)
        }
}
