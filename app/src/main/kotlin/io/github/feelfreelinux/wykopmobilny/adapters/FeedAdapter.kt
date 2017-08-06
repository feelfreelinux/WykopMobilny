package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.adapters.holders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.callbacks.FeedClickCallbackInterface
import io.github.feelfreelinux.wykopmobilny.objects.Entry
import java.util.*


class FeedAdapter(val callbacks : FeedClickCallbackInterface) : RecyclerView.Adapter<EntryViewHolder>() {
    val entryList = ArrayList<Entry>()

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bindView(entryList[position])
    }

    override fun getItemCount() = entryList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder =
            EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false), callbacks)
}
