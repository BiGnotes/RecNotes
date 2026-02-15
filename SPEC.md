# RecNotes - 录音笔记 AI 助手

## 1. 项目概述

**项目名称**: RecNotes  
**项目类型**: 原生 Android 应用  
**核心功能**: 录音笔记 → 语音转写 → AI 智能提取关键信息 → 生成结构化日志 → AI 分析报告

## 2. 功能列表

### 2.1 录音功能
- 一键录音/暂停/停止
- 录音时长显示
- 录音文件本地保存
- 录音列表管理

### 2.2 语音转写
- 集成语音转写 API（ Whisper 或在线服务）
- 转写进度显示
- 转写结果编辑

### 2.3 AI 智能提取
- 自动提取：时间、地点、工作内容、工作时长
- 硅基流动 API (DeepSeek-R1)
- 支持自定义提取规则

### 2.4 日志管理
- 结构化日志存储（Room 数据库）
- 日志列表展示
- 日志编辑/删除
- 日志导出（文本/JSON）

### 2.5 AI 分析报告
- 基于日志内容生成分析报告
- 工作统计（时长、频率等）
- 智能建议

## 3. 技术栈

- **语言**: Kotlin
- **最低 SDK**: 26 (Android 8.0)
- **目标 SDK**: 34 (Android 14)
- **架构**: MVVM + Clean Architecture
- **UI**: Jetpack Compose + Material Design 3
- **依赖注入**: Hilt
- **数据库**: Room
- **网络**: Retrofit + OkHttp
- **协程**: Kotlin Coroutines + Flow
- **AI**: 硅基流动 API (DeepSeek-R1)

## 4. 界面结构

### 4.1 主界面
- 底部导航栏（录音/日志/设置）
- 录音按钮（ FAB）
- 最新日志预览

### 4.2 录音界面
- 大型录音按钮
- 录音波形/时长
- 录音列表

### 4.3 日志界面
- 日志卡片列表
- 搜索/筛选功能
- 日志详情

### 4.4 设置界面
- AI API 配置
- 主题切换
- 数据导出

## 5. GitHub Actions CI/CD

- 每次 push 自动构建 Debug APK
- 生成 APK artifacts 供下载
