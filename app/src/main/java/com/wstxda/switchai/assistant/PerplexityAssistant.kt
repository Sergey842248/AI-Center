package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class PerplexityAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.perplexity.app.android"
    }

    override fun onCreateInternal() {
        onCreateInternal(false)
    }

    override fun onCreateInternal(launchVoiceAssistant: Boolean) {
        if (launchVoiceAssistant) {
            // Try voice assistant first, fallback to main activity if not available
            openAssistant(
                intents = listOf(createPerplexityVoiceIntent(), createPerplexityIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        } else {
            openAssistant(
                intents = listOf(createPerplexityIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        }
    }

    private fun createPerplexityIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "ai.perplexity.app.android.ui.main.MainActivity"
        )
    }

    private fun createPerplexityVoiceIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "ai.perplexity.app.android.assistant.VoiceActivity"
        )
    }
}