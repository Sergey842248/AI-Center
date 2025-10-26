package com.wstxda.switchai

import android.app.Application
import android.content.SharedPreferences
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.ShortcutManager
import com.wstxda.switchai.ui.ThemeManager
import com.wstxda.switchai.utils.Constants

class SwitchAI : Application(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val prefs by lazy { PreferenceHelper(this) }
    private val shortcuts by lazy { ShortcutManager(this) }

    override fun onCreate() {
        super.onCreate()
        ThemeManager.applyTheme(
            prefs.getString(Constants.THEME_PREF_KEY, Constants.THEME_SYSTEM)
                ?: Constants.THEME_SYSTEM
        )
        shortcuts.updateDynamicShortcuts()
        prefs.registerListener(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        prefs.unregisterListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY) {
            shortcuts.updateDynamicShortcuts()
        }

        // Update shortcuts when any preference changes that might affect them
        if (key?.endsWith("_launch_switch") == true ||
            key == Constants.ASSISTANT_SELECTOR_DIALOG_PREF_KEY ||
            key == Constants.ASSISTANT_SEARCH_BAR_PREF_KEY) {
            shortcuts.updateDynamicShortcuts()
        }
    }
}
