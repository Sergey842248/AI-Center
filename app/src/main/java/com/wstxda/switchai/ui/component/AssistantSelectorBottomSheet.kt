package com.wstxda.switchai.ui.component

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.FragmentAssistantDialogBinding
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.adapter.AssistantSelectorAdapter
import com.wstxda.switchai.ui.viewholder.AssistantSelectorItemViewHolder
import com.wstxda.switchai.utils.AssistantsMap
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.AssistantSelectorViewModel

class AssistantSelectorBottomSheet : BaseBottomSheet<FragmentAssistantDialogBinding>() {

    private val viewModel: AssistantSelectorViewModel by viewModels()
    private val preferenceHelper by lazy {
        PreferenceHelper(requireContext())
    }
    private lateinit var assistantSelectorAdapter: AssistantSelectorAdapter

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAssistantDialogBinding.inflate(inflater, container, false)

    override val topDivider: View get() = binding.dividerTop
    override val bottomDivider: View get() = binding.dividerBottom
    override val titleTextView: TextView get() = binding.bottomSheetTitle
    override val titleResId: Int get() = R.string.assistant_select

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupSearch()
    }

    private fun setupSearch() {
        val isSearchBarEnabled =
            preferenceHelper.getBoolean(Constants.ASSISTANT_SEARCH_BAR_PREF_KEY, true)

        binding.searchTextInputLayout.isVisible = isSearchBarEnabled

        if (isSearchBarEnabled) {
            binding.searchEditText.doOnTextChanged { text, _, _, _ ->
                viewModel.searchAssistants(text?.toString())
            }
        }
    }

    private fun setupRecyclerView() {
        assistantSelectorAdapter = AssistantSelectorAdapter(onAssistantClicked = { assistantKey ->
            openAssistant(assistantKey)
            dismiss()
        }, onPinClicked = { assistantKey ->
            viewModel.togglePinAssistant(assistantKey)
        })

        binding.assistantsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = assistantSelectorAdapter
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = if (viewHolder is AssistantSelectorItemViewHolder) {
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN
                } else {
                    0
                }
                return makeMovementFlags(dragFlags, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                viewModel.moveAssistantInList(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewModel.saveAssistantOrder()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.assistantsRecyclerView)
    }

    private fun setupObservers() {
        viewModel.assistantItems.observe(viewLifecycleOwner) { items ->
            assistantSelectorAdapter.submitList(items)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.assistantLoading.isVisible = isLoading
            binding.assistantsRecyclerView.isInvisible = isLoading
        }

        viewModel.searchResultEmpty.observe(viewLifecycleOwner) { isResultEmpty ->
            if (isResultEmpty) {
                binding.searchTextInputLayout.error = getString(R.string.assistant_search_empty)
            } else {
                binding.searchTextInputLayout.error = null
            }
        }
    }

    override fun setupScrollListener() {
        binding.assistantsRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val canScrollUp = lm.findFirstCompletelyVisibleItemPosition() > 0
                val canScrollDown = lm.findLastCompletelyVisibleItemPosition() < assistantSelectorAdapter.itemCount - 1
                updateDividerVisibility(canScrollUp, canScrollDown)
            }
        })
    }

    private fun openAssistant(assistantKey: String) {
        val context = this.context ?: return

        // Check if launch switch is enabled for this assistant
        val launchSwitchKey = getLaunchSwitchKey(assistantKey)
        val isLaunchSwitchEnabled = launchSwitchKey != null &&
            preferenceHelper.getBoolean(launchSwitchKey, false)

        AssistantsMap.assistantActivity[assistantKey]?.let { activityClass ->
            val intent = Intent(context, activityClass).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                // Pass information about whether to launch voice assistant
                putExtra("LAUNCH_VOICE_ASSISTANT", isLaunchSwitchEnabled)
            }
            context.startActivity(intent)
        } ?: Toast.makeText(context, R.string.assistant_open_error, Toast.LENGTH_SHORT).show()
    }

    private fun getLaunchSwitchKey(assistantKey: String): String? {
        return when (assistantKey) {
            "chatgpt_assistant" -> Constants.CHATGPT_LAUNCH_SWITCH
            "claude_assistant" -> Constants.CLAUDE_LAUNCH_SWITCH
            "alexa_assistant" -> Constants.ALEXA_LAUNCH_SWITCH
            "alice_assistant" -> Constants.ALICE_LAUNCH_SWITCH
            "copilot_assistant" -> Constants.COPILOT_LAUNCH_SWITCH
            "deepseek_assistant" -> Constants.DEEPSEEK_LAUNCH_SWITCH
            "doubao_assistant" -> Constants.DOUBAO_LAUNCH_SWITCH
            "gemini_assistant" -> Constants.GEMINI_LAUNCH_SWITCH
            "grok_assistant" -> Constants.GROK_LAUNCH_SWITCH
            "home_assistant" -> Constants.HOME_LAUNCH_SWITCH
            "kimi_assistant" -> Constants.KIMI_LAUNCH_SWITCH
            "le_chat_assistant" -> Constants.LE_CHAT_LAUNCH_SWITCH
            "lumo_assistant" -> Constants.LUMO_LAUNCH_SWITCH
            "manus_assistant" -> Constants.MANUS_LAUNCH_SWITCH
            "marusya_assistant" -> Constants.MARUSYA_LAUNCH_SWITCH
            "minimax_assistant" -> Constants.MINIMAX_LAUNCH_SWITCH
            "delphi_assistant" -> Constants.DELPHI_LAUNCH_SWITCH
            "perplexity_assistant" -> Constants.PERPLEXITY_LAUNCH_SWITCH
            "qingyan_assistant" -> Constants.QINGYAN_LAUNCH_SWITCH
            "qwen_assistant" -> Constants.QWEN_LAUNCH_SWITCH
            "ultimate_alexa_assistant" -> Constants.ULTIMATE_ALEXA_LAUNCH_SWITCH
            "venice_assistant" -> Constants.VENICE_LAUNCH_SWITCH
            "wenxin_yiyan_assistant" -> Constants.WENXIN_YIYAN_LAUNCH_SWITCH
            "yuanbao_assistant" -> Constants.YUANBAO_LAUNCH_SWITCH
            "zapia_assistant" -> Constants.ZAPIA_LAUNCH_SWITCH
            else -> null
        }
    }
}
