package com.recnotes.domain.usecase

import com.recnotes.data.remote.Message
import com.recnotes.data.remote.SiliconFlowApi
import com.recnotes.ui.screens.settings.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProcessTranscriptUseCase @Inject constructor(
    private val api: SiliconFlowApi,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(transcript: String): AIFeedbackResult {
        val apiKey = settingsRepository.getApiKey().first()
        val modelName = settingsRepository.getModelName().first()

        if (apiKey.isEmpty()) {
            return AIFeedbackResult.Error("请先在设置中配置 API Key")
        }

        val prompt = buildPrompt(transcript)

        return try {
            val response = api.chatCompletion(
                authorization = "Bearer $apiKey",
                request = com.recnotes.data.remote.ChatCompletionRequest(
                    model = modelName,
                    messages = listOf(
                        Message(role = "system", content = "你是一个专业的日志分析助手，擅长从录音转写内容中提取关键信息。"),
                        Message(role = "user", content = prompt)
                    )
                )
            )

            val content = response.choices.firstOrNull()?.message?.content ?: ""
            parseAIResponse(content)
        } catch (e: Exception) {
            AIFeedbackResult.Error("AI 处理失败: ${e.message}")
        }
    }

    private fun buildPrompt(transcript: String): String {
        return """
请分析以下录音转写内容，提取关键信息并以 JSON 格式返回：

转写内容：
$transcript

请提取以下信息：
- 时间（日期和时间）
- 地点
- 工作内容
- 工作时长

请以以下 JSON 格式返回：
{
  "time": "提取的时间信息",
  "location": "提取的地点信息",
  "workContent": "提取的工作内容",
  "duration": "提取的工作时长",
  "summary": "一句话总结"
}

只返回 JSON，不要其他内容。
        """.trimIndent()
    }

    private fun parseAIResponse(response: String): AIFeedbackResult {
        return try {
            val json = response.substringAfter("{").substringBeforeLast("}")
            val time = extractJsonValue(json, "time")
            val location = extractJsonValue(json, "location")
            val workContent = extractJsonValue(json, "workContent")
            val duration = extractJsonValue(json, "duration")
            val summary = extractJsonValue(json, "summary")

            AIFeedbackResult.Success(
                time = time,
                location = location,
                workContent = workContent,
                duration = duration,
                summary = summary
            )
        } catch (e: Exception) {
            AIFeedbackResult.Error("解析 AI 响应失败: ${e.message}")
        }
    }

    private fun extractJsonValue(json: String, key: String): String {
        val pattern = """"$key"\s*:\s*"([^"]*)"""".toRegex()
        return pattern.find(json)?.groupValues?.get(1) ?: ""
    }
}

sealed class AIFeedbackResult {
    data class Success(
        val time: String,
        val location: String,
        val workContent: String,
        val duration: String,
        val summary: String
    ) : AIFeedbackResult()

    data class Error(val message: String) : AIFeedbackResult()
}
