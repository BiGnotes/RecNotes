package com.recnotes.ui.screens.recording

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.recnotes.domain.recorder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    private val application: Application
) : AndroidViewModel(application) {

    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()

    private var currentFile: File? = null

    fun toggleRecording() {
        if (_isRecording.value) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        val fileName = "REC_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.m4a"
        // Use external cache dir if available for easier access/debugging, or internal files dir
        val outputDir = application.externalCacheDir ?: application.cacheDir
        val file = File(outputDir, fileName)
        
        try {
            audioRecorder.start(file)
            currentFile = file
            _isRecording.value = true
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error state
        }
    }

    private fun stopRecording() {
        try {
            audioRecorder.stop()
            _isRecording.value = false
            // TODO: Process the recorded file (transcribe, save to DB)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
