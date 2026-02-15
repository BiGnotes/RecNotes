package com.recnotes.ui.screens.recording

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.recnotes.data.repository.LogRepository
import com.recnotes.domain.model.LogEntry
import com.recnotes.domain.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val logRepository: LogRepository,
    private val application: Application
) : AndroidViewModel(application) {

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private var currentFile: File? = null
    private var startTime: Long = 0

    fun toggleRecording() {
        if (_isRecording.value) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        val fileName = "REC_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.m4a"
        val outputDir = application.externalCacheDir ?: application.cacheDir
        val file = File(outputDir, fileName)
        
        try {
            audioRecorder.start(file)
            currentFile = file
            startTime = System.currentTimeMillis()
            _isRecording.value = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        try {
            audioRecorder.stop()
            _isRecording.value = false
            
            val durationMillis = System.currentTimeMillis() - startTime
            
            currentFile?.let { file ->
                val log = LogEntry(
                    title = "Recording ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}",
                    content = "Audio recording",
                    audioPath = file.absolutePath,
                    duration = formatDuration(durationMillis)
                )
                viewModelScope.launch {
                    logRepository.insertLog(log)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun formatDuration(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = millis / (1000 * 60 * 60)
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}
