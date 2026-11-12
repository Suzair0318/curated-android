package com.curated.app.feature.ai

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiPromptHolder @Inject constructor() {
    @Volatile
    var pendingPrompt: String = ""
}
