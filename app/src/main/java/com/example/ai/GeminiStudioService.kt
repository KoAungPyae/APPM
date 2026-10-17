package com.example.ai

import android.content.Context
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

sealed class AiStatus {
    object NotConfigured : AiStatus()
    object Ready : AiStatus()
    data class Error(val message: String) : AiStatus()
}

object GeminiStudioService {
    var appContext: Context? = null

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun getApiKey(): String? {
        appContext?.let { ctx ->
            val prefs = ctx.getSharedPreferences("gemini_prefs", Context.MODE_PRIVATE)
            val savedKey = prefs.getString("gemini_api_key", "")
            if (!savedKey.isNullOrBlank()) {
                return savedKey
            }
        }

        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            val key = field.get(null) as? String
            if (key.isNullOrBlank() || key == "MY_GEMINI_API_KEY" || key.startsWith("YOUR_")) {
                null
            } else {
                key
            }
        } catch (e: Throwable) {
            null
        }
    }

    fun isConfigured(): Boolean = getApiKey() != null

    suspend fun expandStoryIdeaWithAI(
        premise: String,
        genre: String,
        visualStyle: String
    ): Result<String> = expandStoryIdeaWithAi(premise, genre, visualStyle)

    suspend fun expandStoryIdeaWithAi(
        premise: String,
        genre: String,
        visualStyle: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
            ?: return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Please set GEMINI_API_KEY in the Secrets panel.")
            )

        try {
            val prompt = "You are an expert film director and AI video producer.\n" +
                "Given the story premise: \"$premise\"\n" +
                "Genre: \"$genre\"\n" +
                "Visual Style: \"$visualStyle\"\n\n" +
                "Generate a structured story outline with:\n" +
                "1. Logline (1 sentence hook)\n" +
                "2. Main Characters (Name, motivation, archetype)\n" +
                "3. Main Conflict & Theme\n" +
                "4. Climax & Resolution\n\n" +
                "Keep the output clean, cinematic, and actionable for video prompt creation."

            val responseText = callGeminiGenerateContent(apiKey, prompt)
            Result.success(responseText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun enhanceScenePromptWithAI(
        sceneTitle: String,
        action: String,
        visualStyle: String
    ): Result<String> = enhanceScenePromptWithAi(sceneTitle, action, visualStyle)

    suspend fun enhanceScenePromptWithAi(
        sceneTitle: String,
        action: String,
        visualStyle: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
            ?: return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured.")
            )

        try {
            val prompt = "You are a prompt engineer for AI video models (Google Flow, Runway Gen-3, Sora).\n" +
                "Scene: \"$sceneTitle\"\n" +
                "Action: \"$action\"\n" +
                "Style: \"$visualStyle\"\n\n" +
                "Provide enhanced, vivid English camera direction, lighting, and movement description optimized for video generators. Do not invent unrelated plot points."

            val responseText = callGeminiGenerateContent(apiKey, prompt)
            Result.success(responseText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun callGeminiGenerateContent(apiKey: String, promptText: String): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val jsonBody = JSONObject().apply {
            val contents = JSONArray().apply {
                val contentObj = JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().put("text", promptText))
                    }
                    put("parts", parts)
                }
                put(contentObj)
            }
            put("contents", contents)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        val responseString = response.body?.string().orEmpty()

        if (!response.isSuccessful) {
            val errorMsg = try {
                val errorObj = JSONObject(responseString).optJSONObject("error")
                errorObj?.optString("message") ?: "API Error ${response.code}"
            } catch (e: Exception) {
                "HTTP ${response.code}: $responseString"
            }
            throw IllegalStateException(errorMsg)
        }

        val jsonResponse = JSONObject(responseString)
        val candidates = jsonResponse.optJSONArray("candidates")
        if (candidates != null && candidates.length() > 0) {
            val candidate = candidates.getJSONObject(0)
            val content = candidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            if (parts != null && parts.length() > 0) {
                return parts.getJSONObject(0).optString("text")
            }
        }
        return "No response generated from model."
    }
}
