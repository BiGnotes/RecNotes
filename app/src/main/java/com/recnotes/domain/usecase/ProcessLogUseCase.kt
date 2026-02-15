package com.recnotes.domain.usecase

import com.recnotes.data.repository.LogRepository
import com.recnotes.domain.service.TranscriptionService
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessLogUseCase @Inject constructor(
    private val logRepository: LogRepository,
    private val transcriptionService: TranscriptionService,
    private val processTranscriptUseCase: ProcessTranscriptUseCase
) {
    suspend operator fun invoke(logId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val log = logRepository.getLogById(logId) ?: return@withContext Result.failure(Exception("Log not found"))

            // 1. Transcribe
            val transcript = if (log.rawTranscript.isEmpty()) {
                if (log.audioPath.isEmpty()) {
                     return@withContext Result.failure(Exception("No audio file"))
                }
                transcriptionService.transcribe(log.audioPath)
            } else {
                log.rawTranscript
            }

            // Update transcript immediately and remove audio path
            val logWithTranscript = log.copy(
                rawTranscript = transcript,
                audioPath = "" // Clear audio path reference
            )
            logRepository.updateLog(logWithTranscript)

            // Delete local audio file
            try {
                if (log.audioPath.isNotEmpty()) {
                    val audioFile = File(log.audioPath)
                    if (audioFile.exists()) {
                        audioFile.delete()
                    }
                }
            } catch (e: Exception) {
                // Ignore deletion errors, just log locally if possible
            }

            // 2. Analyze (Disabled for now)
            // val feedback = processTranscriptUseCase(transcript)
            // For now, just mark it as transcribed and return
            Result.success(Unit)

            /*
            when (feedback) {
                is AIFeedbackResult.Success -> {
                    val finalLog = logWithTranscript.copy(
                        location = feedback.location,
                        workContent = feedback.workContent,
                        duration = feedback.duration, // Note: This overwrites recorded duration, maybe better to keep recorded one?
                        content = feedback.summary // Update content with summary
                    )
                    logRepository.updateLog(finalLog)
                    Result.success(Unit)
                }
                is AIFeedbackResult.Error -> {
                    Result.failure(Exception(feedback.message))
                }
            }
            */
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
