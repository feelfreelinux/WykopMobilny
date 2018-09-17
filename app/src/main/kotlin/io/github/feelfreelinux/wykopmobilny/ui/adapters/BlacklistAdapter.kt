package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.suggest.SuggestApi
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlacklistBlockViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlacklistViewholder
import javax.inject.Inject

class BlacklistAdapter @Inject constructor(val suggestionApi : SuggestApi) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    var unblockListener : (String) -> Unit = {}
    var blockListener : (String) -> Unit = {}
    var isBlockUser : Boolean = false
    val dataset = ArrayList<String>()

    val VIEW_TYPE_HEADER = 0
    val VIEW_TYPE_ITEMS = 1

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        if (holder is BlacklistViewholder) {
            holder.bind(dataset[position - 1], unblockListener)
        } else if (holder is BlacklistBlockViewHolder) {
            holder.bind(isBlockUser, blockListener, suggestionApi)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER
        else VIEW_TYPE_ITEMS
    }

    override fun getItemCount(): Int = dataset.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder =
            if (viewType == VIEW_TYPE_ITEMS) BlacklistViewholder(LayoutInflater.from(parent.context).inflate(R.layout.blacklist_blocked_item, parent, false))
            else BlacklistBlockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.blacklist_block_form_item, parent, false))
}