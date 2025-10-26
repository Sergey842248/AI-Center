package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class UltimateAlexaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.customsolutions.android.alexa"
    }

    override fun onCreateInternal() {
        onCreateInternal(false)
    }

    override fun onCreateInternal(launchVoiceAssistant: Boolean) {
        if (launchVoiceAssistant) {
            // Try voice assistant first, fallback to main activity if not available
            openAssistant(
                intents = listOf(createUltimateAlexaVoiceIntent(), createUltimateAlexaIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        } else {
            openAssistant(
                intents = listOf(createUltimateAlexaIntent()),
                errorMessage = R.string.assistant_application_not_found
            )
        }
    }

    private fun createUltimateAlexaIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.customsolutions.android.alexa.MainActivity"
        )
    }

    private fun createUltimateAlexaVoiceIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.customsolutions.android.alexa.VoiceActivity"
        )
    }
}
