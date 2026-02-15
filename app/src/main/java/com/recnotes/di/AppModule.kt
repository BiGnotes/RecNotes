package com.recnotes.di

import android.content.Context
import com.recnotes.data.recorder.AndroidAudioRecorder
import com.recnotes.domain.recorder.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTranscriptionService(
        api: com.recnotes.data.remote.GroqApi,
        settingsRepository: com.recnotes.ui.screens.settings.SettingsRepository
    ): com.recnotes.domain.service.TranscriptionService {
        return com.recnotes.data.service.GroqTranscriptionService(api, settingsRepository)
    }

    @Provides
    @Singleton
    fun provideAudioRecorder(@ApplicationContext context: Context): AudioRecorder {
        return AndroidAudioRecorder(context)
    }
}
