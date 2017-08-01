package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.adapters.holders.FeedViewHolder
import io.github.feelfreelinux.wykopmobilny.objects.Entry

typealias VoteClickListener = (Entry, (Boolean, Int) -> Unit) -> Unit
typealias TagClickListener = (String) -> Unit
typealias CommentClickListener = (Int) -> Unit

interface IFeedAdapterActions {
    val tagClickListener: TagClickListener
    val commentClickListener: CommentClickListener
}

class FeedAdapter(val actions : IFeedAdapterActions) : RecyclerView.Adapter<FeedViewHolder>() {

    var entryList = emptyList<Entry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.tagClickListener = actions.tagClickListener
        holder.commentClickListener = actions.commentClickListener
        holder.bindItem(entryList[position])
    }

    override fun getItemCount() = entryList.size
}