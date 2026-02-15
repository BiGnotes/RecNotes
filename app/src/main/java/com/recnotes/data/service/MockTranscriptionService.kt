package com.recnotes.data.service

import com.recnotes.domain.service.TranscriptionService
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockTranscriptionService @Inject constructor() : TranscriptionService {

    override suspend fun transcribe(audioPath: String): String {
        delay(2000) // Simulate processing time
        return "2024年2月15日，我们在会议室讨论了 RecNotes 项目的进展。主要内容包括：1. 确认了录音功能已经实现。2. 正在开发 AI 转写和分析功能。3. 计划下周发布测试版。会议持续了约30分钟，大家都非常积极。"
    }
}
