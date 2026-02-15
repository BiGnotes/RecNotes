package com.recnotes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.recnotes.domain.model.LogEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Query("SELECT * FROM logs ORDER BY createdAt DESC")
    fun getAllLogs(): Flow<List<LogEntry>>

    @Query("SELECT * FROM logs WHERE id = :id")
    suspend fun getLogById(id: Long): LogEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogEntry): Long

    @Update
    suspend fun updateLog(log: LogEntry)

    @Delete
    suspend fun deleteLog(log: LogEntry)

    @Query("DELETE FROM logs WHERE id = :id")
    suspend fun deleteLogById(id: Long)
}
