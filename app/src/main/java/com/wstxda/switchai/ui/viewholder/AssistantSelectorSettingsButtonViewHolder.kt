package com.wstxda.switchai.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemSettingsButtonBinding

class AssistantSelectorSettingsButtonViewHolder(
    private val binding: ListItemSettingsButtonBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(onSettingsButtonClicked: () -> Unit) {
        binding.openSettingsButton.setOnClickListener {
            onSettingsButtonClicked()
        }
    }
}
