package com.recnotes.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _groqApiKey = MutableStateFlow("")
    val groqApiKey: StateFlow<String> = _groqApiKey.asStateFlow()

    private val _modelName = MutableStateFlow("deepseek-ai/DeepSeek-R1-0528-Qwen3-8B")
    val modelName: StateFlow<String> = _modelName.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getApiKey().collect { key ->
                _apiKey.value = key
            }
        }
        viewModelScope.launch {
            settingsRepository.getGroqApiKey().collect { key ->
                _groqApiKey.value = key
            }
        }
        viewModelScope.launch {
            settingsRepository.getModelName().collect { model ->
                _modelName.value = model
            }
        }
    }

    fun setApiKey(key: String) {
        _apiKey.value = key
    }

    fun setGroqApiKey(key: String) {
        _groqApiKey.value = key
    }

    fun setModelName(model: String) {
        _modelName.value = model
    }

    fun saveSettings() {
        viewModelScope.launch {
            settingsRepository.saveApiKey(_apiKey.value)
            settingsRepository.saveGroqApiKey(_groqApiKey.value)
            settingsRepository.saveModelName(_modelName.value)
        }
    }
}
