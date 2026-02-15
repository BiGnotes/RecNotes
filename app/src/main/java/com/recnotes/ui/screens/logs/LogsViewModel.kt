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

    fun processLog(logId: Long) {
        viewModelScope.launch {
            processLogUseCase(logId)
        }
    }
}
