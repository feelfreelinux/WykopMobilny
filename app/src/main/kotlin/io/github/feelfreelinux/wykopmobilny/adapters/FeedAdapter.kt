package io.github.feelfreelinux.wykopmobilny.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.objects.Entry

typealias VoteClickListener = (Entry, Boolean, (Boolean) -> Unit) -> Unit


class FeedAdapter(
        private val entryVoteClickListener: VoteClickListener,
        private val tagClickListener: TagClickListener,
        private val commentClickListener: CommentClickListener
) : RecyclerView.Adapter<FeedViewHolder>() {

    var dataSet = emptyList<Entry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_layout, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bindItem(dataSet[position])
        holder.commentClickListener = commentClickListener
        holder.tagClickListener = tagClickListener
        holder.entryVoteClickListener = entryVoteClickListener
    }

    override fun getItemCount() = dataSet.size
}