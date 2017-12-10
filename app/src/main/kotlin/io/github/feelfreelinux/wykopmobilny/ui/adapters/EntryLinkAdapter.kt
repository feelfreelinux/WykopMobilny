package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.AdvancedProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder

class EntryLinkAdapter() : AdvancedProgressAdapter<EntryLink>() {
    companion object {
        val ENTRY_VIEWTYPE = 1
        val LINK_VIEWTYPE = 2
    }

    override fun getItemViewType(position: Int): Int = when {
        dataset[position] == null -> AdvancedProgressAdapter.VIEWTYPE_PROGRESS
        dataset[position]!!.entry != null -> ENTRY_VIEWTYPE
        else -> LINK_VIEWTYPE
    }

    override fun createViewHolder(viewType: Int, parent: ViewGroup): RecyclerView.ViewHolder =
            when (viewType) {
                ENTRY_VIEWTYPE -> EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false))
                else -> LinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_layout, parent, false))
            }

    override fun bindHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]!!
        if (item.entry != null) {
            (holder as EntryViewHolder).bindView(item.entry, true)
        } else if (item.link != null) {
            (holder as LinkViewHolder).bindView(item.link)
        }
    }

}
