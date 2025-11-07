package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class HuggingAssist : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "org.woheller69.hugassist"
    }

    override fun onCreateInternal() {
        onCreateInternal(false)
    }

    override fun onCreateInternal(launchVoiceAssistant: Boolean) {
        if (launchVoiceAssistant) {
            // Try voice assistant first, fallback to main activity if not available
            openAssistant(
                intents = listOf(createHuggingVoiceIntent(), createHuggingIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        } else {
            openAssistant(
                intents = listOf(createHuggingIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        }
    }

    private fun createHuggingIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "org.woheller69.huggingchat.MainActivity"
        )
    }

    private fun createHuggingVoiceIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "org.woheller69.huggingchat.MainActivity"
        )
    }
}
