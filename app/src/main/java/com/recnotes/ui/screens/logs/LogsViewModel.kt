package com.recnotes.ui.screens.logs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.recnotes.domain.model.LogEntry
import com.recnotes.data.repository.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val repository: LogRepository,
    private val processLogUseCase: com.recnotes.domain.usecase.ProcessLogUseCase
) : ViewModel() {

    val logs: StateFlow<List<LogEntry>> = repository.getAllLogs()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _editingLog = kotlinx.coroutines.flow.MutableStateFlow<LogEntry?>(null)
    val editingLog: StateFlow<LogEntry?> = _editingLog

    fun processLog(logId: Long) {
        viewModelScope.launch {
            val result = processLogUseCase(logId)
            result.onSuccess {
                // Fetch the updated log to open in editor
                val updatedLog = repository.getLogById(logId)
                _editingLog.value = updatedLog
            }
        }
    }

    fun startEditing(log: LogEntry) {
        _editingLog.value = log
    }

    fun dismissEditing() {
        _editingLog.value = null
    }

    fun saveTranscript(log: LogEntry, newTranscript: String) {
        viewModelScope.launch {
            val updatedLog = log.copy(rawTranscript = newTranscript)
            repository.updateLog(updatedLog)
            _editingLog.value = null
        }
    }
}
