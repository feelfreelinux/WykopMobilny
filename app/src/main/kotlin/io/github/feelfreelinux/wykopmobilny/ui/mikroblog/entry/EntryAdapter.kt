package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.ui.holders.CommentViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.holders.EntryViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.FeedClickCallbackInterface
import io.github.feelfreelinux.wykopmobilny.api.Entry


class EntryAdapter(val callbacks: FeedClickCallbackInterface) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var entry : Entry? = null
    private val ENTRY_HOLDER = 0
    private val COMMENT_HOLDER = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder?.itemViewType) {
            ENTRY_HOLDER -> (holder as EntryViewHolder).bindView(entry!!)
            COMMENT_HOLDER -> (holder as CommentViewHolder).bindView(entry!!.comments!![position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) return ENTRY_HOLDER
        else return COMMENT_HOLDER
    }

    override fun getItemCount() : Int {
        entry?.comments?.let {
            return it.size + 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ENTRY_HOLDER -> EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false), callbacks)
            else -> CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_layout, parent, false), callbacks)
        }
    }
}
