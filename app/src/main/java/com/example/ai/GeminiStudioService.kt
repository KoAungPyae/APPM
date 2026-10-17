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

    /**
     * Checks if a valid GEMINI_API_KEY is configured in AI Studio Secrets panel / BuildConfig
     */
    fun getApiKey(): String? {
        // SharedPreferences ထဲက User ထည့်ထားတဲ့ Key ကို ဦးစားပေးဆွဲယူခြင်း
        appContext?.let { ctx ->
            val prefs = ctx.getSharedPreferences("gemini_prefs", Context.MODE_PRIVATE)
            val savedKey = prefs.getString("gemini_api_key", "")
            if (!savedKey.isNullOrBlank()) {
                return savedKey
            }
        }

        // BuildConfig ထဲတွင် Key ရှိပါက ဆွဲယူခြင်း (Fallback)
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

    /**
     * Expands a story idea or generates screenplay scenes using Gemini REST API
     * if the user has provided a real key in the Secrets panel.
     */
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
            val prompt = """
                You are an expert film director and AI video producer
