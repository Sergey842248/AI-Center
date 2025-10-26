package com.wstxda.switchai.activity

import android.os.Bundle

abstract class AssistantActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if voice assistant launch is requested
        val launchVoiceAssistant = intent.getBooleanExtra("LAUNCH_VOICE_ASSISTANT", false)
        onCreateInternal(launchVoiceAssistant)
        finish()
    }

    abstract fun onCreateInternal()
    open fun onCreateInternal(launchVoiceAssistant: Boolean) {
        // Default implementation calls the original method
        onCreateInternal()
    }
}
