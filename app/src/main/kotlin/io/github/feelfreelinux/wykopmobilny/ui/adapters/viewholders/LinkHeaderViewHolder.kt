package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import kotlinx.android.synthetic.main.link_details_header_list_item.view.*

class LinkHeaderViewHolder(val view : View) : RecyclerView.ViewHolder(view) {
    fun bindView(link : Link) {
        view.linkHeader.setLinkData(link)
    }
}