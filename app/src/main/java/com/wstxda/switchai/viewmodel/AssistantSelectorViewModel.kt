package com.wstxda.switchai.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.wstxda.switchai.R
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.logic.PackageChecker
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView
import com.wstxda.switchai.ui.utils.AssistantResourcesManager
import com.wstxda.switchai.utils.AssistantsMap
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AssistantSelectorViewModel(application: Application) : AndroidViewModel(application),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val _assistantItems = MutableLiveData<List<AssistantSelectorRecyclerView>>()
    val assistantItems: LiveData<List<AssistantSelectorRecyclerView>> = _assistantItems

    private var allAssistantItems: List<AssistantSelectorRecyclerView> = emptyList()

    private val _searchResultEmpty = MutableLiveData<Boolean>()
    val searchResultEmpty: LiveData<Boolean> = _searchResultEmpty

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val pinnedAssistantKeys = mutableSetOf<String>()

    private val assistantStatePreferences by lazy {
        getApplication<Application>().getSharedPreferences(
            Constants.PREFS_NAME, Application.MODE_PRIVATE
        )
    }

    private val preferenceHelper: PreferenceHelper =
        PreferenceHelper(application.applicationContext)

    private val defaultSharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application.applicationContext)

    private val assistantResourcesManager: AssistantResourcesManager =
        AssistantResourcesManager(application.applicationContext)

    private val packageChecker: PackageChecker = PackageChecker(application.applicationContext)

    init {
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this)
        loadAssistants()
    }

    private fun loadAssistants() {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                loadStateFromPreferences()
                val installedKeys = packageChecker.installedAssistants()
                val allVisibleAssistantDetails = getVisibleAssistantDetails(installedKeys)
                val categorizedItems = buildCategorizedList(allVisibleAssistantDetails)
                withContext(Dispatchers.Main) {
                    allAssistantItems = categorizedItems
                    _assistantItems.value = categorizedItems
                }
            }
            _isLoading.value = false
        }
    }

    fun searchAssistants(query: String?) {
        if (query.isNullOrBlank()) {
            _assistantItems.value = allAssistantItems
            _searchResultEmpty.value = false
            return
        }

        val filteredList = allAssistantItems.filterIsInstance<AssistantSelectorRecyclerView.AssistantSelector>()
                .filter { assistant ->
                    assistant.assistantItem.name.contains(query, ignoreCase = true)
                }

        _assistantItems.value = filteredList
        _searchResultEmpty.value = filteredList.isEmpty()
    }

    fun togglePinAssistant(assistantKey: String) {
        viewModelScope.launch {
            val currentAssistantItems =
                _assistantItems.value?.filterIsInstance<AssistantSelectorRecyclerView.AssistantSelector>()
                    ?.map { it.assistantItem } ?: return@launch

            val isCurrentlyPinned = pinnedAssistantKeys.contains(assistantKey)

            val updatedAssistantItems = currentAssistantItems.map { item ->
                if (item.key == assistantKey) {
                    item.copy(isPinned = !isCurrentlyPinned)
                } else {
                    item
                }
            }

            withContext(Dispatchers.IO) {
                if (isCurrentlyPinned) {
                    pinnedAssistantKeys.remove(assistantKey)
                } else {
                    pinnedAssistantKeys.add(assistantKey)
                }
                assistantStatePreferences.edit {
                    putStringSet(Constants.CAT_PINNED_ASSISTANTS_KEY, pinnedAssistantKeys)
                }
            }

            val newCategorizedList = buildCategorizedList(updatedAssistantItems)
            _assistantItems.value = newCategorizedList
        }
    }



    private fun loadStateFromPreferences() {
        val loadedPinnedKeys = assistantStatePreferences.getStringSet(
            Constants.CAT_PINNED_ASSISTANTS_KEY, emptySet()
        ) ?: emptySet()
        pinnedAssistantKeys.clear()
        pinnedAssistantKeys.addAll(loadedPinnedKeys)
    }

    private fun getVisibleAssistantDetails(installedKeys: Set<String>): List<AssistantItem> {
        val context = getApplication<Application>().applicationContext
        val defaultVisibleAssistants =
            context.resources.getStringArray(R.array.assistant_visibility_values).toSet()
        val visibleAssistantKeys = preferenceHelper.getStringSet(
            Constants.ASSISTANT_MANAGER_DIALOG_PREF_KEY, defaultVisibleAssistants
        )

        return AssistantsMap.assistantActivity.filterKeys { it in visibleAssistantKeys }
            .map { (key, _) ->
                AssistantItem(
                    key = key,
                    name = assistantResourcesManager.getAssistantName(key),
                    iconRes = assistantResourcesManager.getAssistantIcon(key),
                    isInstalled = key in installedKeys,
                    isPinned = key in pinnedAssistantKeys,
                    lastUsedTime = 0L
                )
            }
    }

    private fun buildCategorizedList(assistants: List<AssistantItem>): List<AssistantSelectorRecyclerView> {
        val context = getApplication<Application>().applicationContext
        val (installedAssistants, notInstalledAssistants) = assistants.partition { it.isInstalled }
        return buildList {
            val groupedByPinned = installedAssistants.groupBy { it.isPinned }
            val pinnedItems = (groupedByPinned[true] ?: emptyList()).sortedBy { it.name }
                .map { AssistantSelectorRecyclerView.AssistantSelector(it) }

            val otherItems = groupedByPinned[false] ?: emptyList()

            if (pinnedItems.isNotEmpty()) {
                add(AssistantSelectorRecyclerView.CategoryHeader(context.getString(R.string.assistant_category_pin)))
                addAll(pinnedItems)
            }

            if (otherItems.isNotEmpty()) {
                add(AssistantSelectorRecyclerView.CategoryHeader(context.getString(R.string.assistant_category_all)))
                val sortedOthers = otherItems.sortedBy { it.name }
                    .map { AssistantSelectorRecyclerView.AssistantSelector(it) }
                addAll(sortedOthers)
            }
            if (notInstalledAssistants.isNotEmpty()) {
                add(AssistantSelectorRecyclerView.CategoryHeader(context.getString(R.string.assistant_category_not_installed)))
                val sortedNotInstalled = notInstalledAssistants.sortedBy { it.name }
                    .map { AssistantSelectorRecyclerView.AssistantSelector(it) }
                addAll(sortedNotInstalled)
            }
        }
    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Constants.ASSISTANT_MANAGER_DIALOG_PREF_KEY) {
            loadAssistants()
        }
    }

    override fun onCleared() {
        super.onCleared()
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}
