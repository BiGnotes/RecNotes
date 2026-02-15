package com.recnotes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.recnotes.domain.model.LogEntry

@Database(
    entities = [LogEntry::class],
    version = 1,
    exportSchema = false
)
abstract class RecNotesDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}
