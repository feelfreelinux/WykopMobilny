package io.github.wykopmobilny.promoted

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.promoted.api.Link

class PromotedAdapter : PagingDataAdapter<Link, RecyclerView.ViewHolder>(
    diffCallback = LinkDiff(),
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }
}

class LinkDiff : DiffUtil.ItemCallback<Link>() {

    override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Link, newItem: Link) = oldItem == newItem
}
