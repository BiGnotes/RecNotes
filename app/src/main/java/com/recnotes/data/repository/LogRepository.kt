package com.recnotes.data.repository

import com.recnotes.data.local.LogDao
import com.recnotes.domain.model.LogEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository @Inject constructor(
    private val logDao: LogDao
) {
    fun getAllLogs(): Flow<List<LogEntry>> = logDao.getAllLogs()

    suspend fun getLogById(id: Long): LogEntry? = logDao.getLogById(id)

    suspend fun insertLog(log: LogEntry): Long = logDao.insertLog(log)

    suspend fun updateLog(log: LogEntry) = logDao.updateLog(log)

    suspend fun deleteLog(log: LogEntry) = logDao.deleteLog(log)

    suspend fun deleteLogById(id: Long) = logDao.deleteLogById(id)
}
