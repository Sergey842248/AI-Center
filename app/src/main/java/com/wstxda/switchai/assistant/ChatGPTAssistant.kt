package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ChatGPTAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.openai.chatgpt"
    }

    override fun onCreateInternal() {
        onCreateInternal(false)
    }

    override fun onCreateInternal(launchVoiceAssistant: Boolean) {
        if (launchVoiceAssistant) {
            // Try voice assistant first, fallback to main activity if not available
            openAssistant(
                intents = listOf(createChatGPTVoiceIntent(), createChatGPTIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        } else {
            openAssistant(
                intents = listOf(createChatGPTIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        }
    }

    private fun createChatGPTIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.openai.chatgpt.MainActivity"
        )
    }

    private fun createChatGPTVoiceIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.openai.voice.assistant.AssistantActivity"
        )
    }
}
