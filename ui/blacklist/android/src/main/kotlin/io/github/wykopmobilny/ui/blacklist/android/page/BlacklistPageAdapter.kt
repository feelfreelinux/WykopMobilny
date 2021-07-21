package io.github.wykopmobilny.ui.blacklist.android.page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.ui.blacklist.BlacklistedElementUi
import io.github.wykopmobilny.ui.blacklist.android.R
import io.github.wykopmobilny.ui.blacklist.android.databinding.ItemBlockedElementBinding

internal class BlacklistPageAdapter : ListAdapter<BlacklistedElementUi, BlacklistPageAdapter.DefaultViewHolder>(BlacklistElementDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        val binding = ItemBlockedElementBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DefaultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.name.text = item.name
        when (val state = item.state) {
            is BlacklistedElementUi.StateUi.Default -> {
                holder.binding.progress.isVisible = false
                holder.binding.btnAction.isVisible = true
                holder.binding.btnAction.setImageResource(R.drawable.ic_lock_open)
                holder.binding.btnAction.setOnClickListener { state.unblock() }
            }
            is BlacklistedElementUi.StateUi.Error -> {
                holder.binding.progress.isVisible = false
                holder.binding.btnAction.isVisible = true
                holder.binding.btnAction.setImageResource(R.drawable.ic_exclamation_round)
                holder.binding.btnAction.setOnClickListener { state.showError() }
            }
            BlacklistedElementUi.StateUi.InProgress -> {
                holder.binding.progress.isVisible = true
                holder.binding.btnAction.isVisible = false
                holder.binding.btnAction.setOnClickListener(null)
            }
        }
    }

    class DefaultViewHolder(val binding: ItemBlockedElementBinding) : RecyclerView.ViewHolder(binding.root)

    private class BlacklistElementDiff : DiffUtil.ItemCallback<BlacklistedElementUi>() {

        override fun areItemsTheSame(oldItem: BlacklistedElementUi, newItem: BlacklistedElementUi) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: BlacklistedElementUi, newItem: BlacklistedElementUi) =
            oldItem.name == newItem.name && oldItem.state::class == newItem.state::class
    }
}
