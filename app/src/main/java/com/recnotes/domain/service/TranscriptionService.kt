package com.recnotes.domain.service

interface TranscriptionService {
    suspend fun transcribe(audioPath: String): String
}
