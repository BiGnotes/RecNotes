package com.recnotes.di

import android.content.Context
import androidx.room.Room
import com.recnotes.data.local.LogDao
import com.recnotes.data.local.RecNotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RecNotesDatabase {
        return Room.databaseBuilder(
            context,
            RecNotesDatabase::class.java,
            "recnotes_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLogDao(database: RecNotesDatabase): LogDao {
        return database.logDao()
    }
}
