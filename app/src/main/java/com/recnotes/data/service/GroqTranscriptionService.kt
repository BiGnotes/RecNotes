package com.recnotes.data.service

import com.recnotes.data.remote.GroqApi
import com.recnotes.domain.service.TranscriptionService
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.recnotes.ui.screens.settings.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GroqTranscriptionService @Inject constructor(
    private val api: GroqApi,
    private val settingsRepository: SettingsRepository
) : TranscriptionService {

    override suspend fun transcribe(audioPath: String): String {
        var apiKey = settingsRepository.getGroqApiKey().first()
        if (apiKey.isEmpty()) {
            // Fallback to BuildConfig if available (injected via CI/CD)
            apiKey = com.recnotes.BuildConfig.GROQ_API_KEY
        }
        
        if (apiKey.isEmpty() || apiKey == "null") {
            throw Exception("Please configure Groq API Key in settings")
        }
        val authorization = "Bearer $apiKey"

        val file = File(audioPath)
        if (!file.exists()) {
            throw Exception("Audio file not found: $audioPath")
        }

        val requestFile = file.asRequestBody("audio/m4a".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val model = "whisper-large-v3-turbo".toRequestBody("text/plain".toMediaTypeOrNull())
        val language = "zh".toRequestBody("text/plain".toMediaTypeOrNull())

        return try {
            val response = api.transcribeAudio(
                authorization = authorization,
                file = body,
                model = model,
                language = language
            )
            response.text
        } catch (e: Exception) {
            throw Exception("Transcription failed: ${e.message}")
        }
    }
}
