package com.recnotes.domain.usecase

import com.recnotes.data.repository.LogRepository
import com.recnotes.domain.service.TranscriptionService
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

            // Update transcript immediately
            val logWithTranscript = log.copy(rawTranscript = transcript)
            logRepository.updateLog(logWithTranscript)

            // 2. Analyze
            val feedback = processTranscriptUseCase(transcript)

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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
