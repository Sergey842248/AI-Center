package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantRoot
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.launch

class MarusyaAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "ru.mail.search.electroscope"
    }

    override fun onCreateInternal() {
        onCreateInternal(false)
    }

    override fun onCreateInternal(launchVoiceAssistant: Boolean) {
        lifecycleScope.launch {
            if (launchVoiceAssistant || preferences.getBoolean(Constants.ASSISTANT_ROOT_PREF_KEY)) {
                openMarusyaAssistant()
            } else {
                openMarusya()
            }
        }
    }

    private fun openMarusyaAssistant() {
        openAssistantRoot(
            intents = listOf(createMarusyaAssistantIntent()),
            rootAccessMessage = R.string.root_access_warning,
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun openMarusya() {
        openAssistant(
            intents = listOf(createMarusyaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMarusyaIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "ru.mail.search.electroscope.ui.activity.AssistantActivity"
        )
    }

    private fun createMarusyaAssistantIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName,
            "ru.mail.search.electroscope.defaultassistant.presentation.keyguard.DefaultAssistantSessionActivity"
        )
    }
}