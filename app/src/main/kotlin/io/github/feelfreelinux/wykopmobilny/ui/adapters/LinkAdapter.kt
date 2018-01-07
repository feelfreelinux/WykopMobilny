package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.SimpleBaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.LinkViewHolder

class LinkAdapter : SimpleBaseProgressAdapter<LinkViewHolder, Link>() {
    override fun bindHolder(holder: LinkViewHolder, position: Int) {
        holder.bindView(data[position])
    }

    override fun createViewHolder(parent: ViewGroup): LinkViewHolder =
            LinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_layout, parent, false))
}