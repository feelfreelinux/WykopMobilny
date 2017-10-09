package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import kotlinx.android.synthetic.main.entry_list_item.view.*


class EntryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(entry : Entry) {
        view.entry.setEntryData(entry)
    }
}