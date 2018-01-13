package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.LinkPresenter
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_details_header_list_item.view.*

class LinkHeaderViewHolder(val view : View, val linkPresenter: LinkPresenter, val userManagerApi: UserManagerApi) : RecyclerView.ViewHolder(view) {
    fun bindView(link : Link) {
        view.linkHeader.setLinkData(link, linkPresenter, userManagerApi)
    }
}