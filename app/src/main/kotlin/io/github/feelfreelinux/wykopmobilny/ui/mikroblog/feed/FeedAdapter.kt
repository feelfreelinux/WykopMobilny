package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.ui.elements.holders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler.WykopActionHandler

class FeedAdapter(val callbacks : WykopActionHandler) : BaseProgressAdapter<EntryViewHolder, Entry>() {
    override fun bindHolder(holder: EntryViewHolder, position: Int) {
        holder.bindView(dataset[position]!!)
    }

    fun addData(entries : List<Entry>, shouldClearAdapter : Boolean) {
        isLoading = false

        if (shouldClearAdapter) dataset.clear()
        dataset.addAll(entries)

        if(shouldClearAdapter) notifyDataSetChanged()
        else notifyItemRangeInserted(
                dataset.size,
                dataset.size + entries.size
        )

    }

    override fun createViewHolder(parent: ViewGroup): EntryViewHolder =
            EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false), callbacks)
}