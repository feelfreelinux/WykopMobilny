package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.adapters.holders.EntryDetailsViewHolder
import io.github.feelfreelinux.wykopmobilny.objects.Entry

interface IEntryDetailsActions {
    val tagClickListener: TagClickListener
}

class EntryDetailsAdapter(val actions : IFeedAdapterActions) : RecyclerView.Adapter<EntryDetailsViewHolder>() {

    var entryData = emptyList<Entry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: EntryDetailsViewHolder, position: Int) {
        holder.tagClickListener = actions.tagClickListener
        holder.bindView(entryData[position])
    }

    override fun getItemCount() = entryData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entry_layout, parent, false)
        return EntryDetailsViewHolder(view)
    }
}
