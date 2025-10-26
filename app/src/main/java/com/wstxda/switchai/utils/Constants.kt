package com.wstxda.switchai.utils

object Constants {

    // Preferences keys

    const val DIGITAL_ASSISTANT_SETUP_PREF_KEY = "digital_assistant_setup"
    const val DIGITAL_ASSISTANT_SELECT_PREF_KEY = "digital_assistant_select"
    const val ASSISTANT_SELECTOR_DIALOG_PREF_KEY = "assistant_selector_dialog"
    const val ASSISTANT_SEARCH_BAR_PREF_KEY = "assistant_search_bar"
    const val ASSISTANT_MANAGER_DIALOG_PREF_KEY = "assistant_selector_manager"
    const val OPEN_ASSISTANT_TILE_PREF_KEY = "open_assistant_tile"
    const val OPEN_ASSISTANT_WIDGET_PREF_KEY = "open_assistant_widget"
    const val ASSISTANT_VIBRATION_PREF_KEY = "assistant_vibration"
    const val ASSISTANT_SOUND_PREF_KEY = "assistant_sound"
    const val ASSISTANT_ROOT_PREF_KEY = "assistant_root"
    const val LIBRARY_PREF_KEY = "library"
    const val THEME_PREF_KEY = "select_theme"

    // AI Launch Switch Keys
    const val ALEXA_LAUNCH_SWITCH = "alexa_launch_switch"
    const val ALICE_LAUNCH_SWITCH = "alice_launch_switch"
    const val CHATGPT_LAUNCH_SWITCH = "chatgpt_launch_switch"
    const val CLAUDE_LAUNCH_SWITCH = "claude_launch_switch"
    const val COPILOT_LAUNCH_SWITCH = "copilot_launch_switch"
    const val DEEPSEEK_LAUNCH_SWITCH = "deepseek_launch_switch"
    const val DOUBAO_LAUNCH_SWITCH = "doubao_launch_switch"
    const val GEMINI_LAUNCH_SWITCH = "gemini_launch_switch"
    const val GROK_LAUNCH_SWITCH = "grok_launch_switch"
    const val HOME_LAUNCH_SWITCH = "home_launch_switch"
    const val KIMI_LAUNCH_SWITCH = "kimi_launch_switch"
    const val LE_CHAT_LAUNCH_SWITCH = "le_chat_launch_switch"
    const val LUMO_LAUNCH_SWITCH = "lumo_launch_switch"
    const val MANUS_LAUNCH_SWITCH = "manus_launch_switch"
    const val MARUSYA_LAUNCH_SWITCH = "marusya_launch_switch"
    const val META_LAUNCH_SWITCH = "meta_launch_switch"
    const val MINIMAX_LAUNCH_SWITCH = "minimax_launch_switch"
    const val DELPHI_LAUNCH_SWITCH = "delphi_launch_switch"
    const val PERPLEXITY_LAUNCH_SWITCH = "perplexity_launch_switch"
    const val QINGYAN_LAUNCH_SWITCH = "qingyan_launch_switch"
    const val QWEN_LAUNCH_SWITCH = "qwen_launch_switch"
    const val ULTIMATE_ALEXA_LAUNCH_SWITCH = "ultimate_alexa_launch_switch"
    const val VENICE_LAUNCH_SWITCH = "venice_launch_switch"
    const val WENXIN_YIYAN_LAUNCH_SWITCH = "wenxin_yiyan_launch_switch"
    const val YUANBAO_LAUNCH_SWITCH = "yuanbao_launch_switch"
    const val ZAPIA_LAUNCH_SWITCH = "zapia_launch_switch"

    // Theme values

    const val THEME_SYSTEM = "system"
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"

    // Used to hide category view with digital assistant tile preference

    const val SETTINGS_CATEGORY_SHORTCUTS = "shortcuts"

    // Dialog fragments

    const val ASSISTANT_MANAGER_DIALOG = "AssistantManagerDialog"
    const val DIGITAL_ASSISTANT_DIALOG = "DigitalAssistantSetupDialog"
    const val DIGITAL_ASSISTANT_SELECTOR_DIALOG = "AssistantSelectorBottomSheet"
    const val TUTORIAL_DIALOG = "AssistantTutorialBottomSheet"
    const val PREFERENCE_DIALOG = "PreferenceDialog"

    // Constants for preferences

    const val IS_ASSIST_SETUP_DONE = "is_assist_setup_done"
    const val PREFS_NAME = "assistant_selector_prefs"

    // Logs tags

    const val ROOT_CHECKER = "RootChecker"

    // AssistantSelector recyclerView

    const val VIEW_TYPE_CATEGORY_HEADER = 0
    const val VIEW_TYPE_ASSISTANT_ITEM = 1
    // Order for AssistantSelectorBottomSheet
    const val PINNED_ASSISTANTS_ORDER_KEY = "pinned_assistants_order"
    const val UNPINNED_ASSISTANTS_ORDER_KEY = "unpinned_assistants_order"

    // Widget Material assistant action

    const val ACTION_ASSISTANT_SELECTED = "com.wstxda.switchai.ACTION_ASSISTANT_SELECTED"

    // GitHub API releases URL

    const val GITHUB_RELEASE_URL = "https://api.github.com/repos/Sergey842248/AI-Center/releases/latest"
}
