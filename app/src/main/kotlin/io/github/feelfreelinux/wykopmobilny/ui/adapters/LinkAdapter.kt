package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.utils.printout
import javax.inject.Inject

class LinkAdapter @Inject constructor() : SimpleBaseProgressAdapter<LinkViewHolder, Link>() {
    override fun bindHolder(holder: LinkViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    override fun createViewHolder(parent: ViewGroup): LinkViewHolder {
        val currentTimeMilist = System.currentTimeMillis()
        val ddd = LinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_layout, parent, false))
        printout((System.currentTimeMillis() - currentTimeMilist).toString())
        return ddd

    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}