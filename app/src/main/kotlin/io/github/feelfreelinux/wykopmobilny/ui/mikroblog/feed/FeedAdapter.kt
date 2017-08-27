package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.holders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.base.BaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.api.Entry

class FeedAdapter(val callbacks : FeedClickCallbackInterface) : BaseProgressAdapter<EntryViewHolder, Entry>() {
    override fun bindHolder(holder: EntryViewHolder, position: Int) {
        holder.bindView(dataset[position]!!)
    }

    override fun createViewHolder(parent: ViewGroup): EntryViewHolder =
            EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false), callbacks)
}
