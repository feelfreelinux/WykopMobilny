package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi
import kotlinx.android.synthetic.main.conversation_list_item.view.*
import kotlinx.android.synthetic.main.observed_tag_list_item.view.*

class ObservedTagViewHolder(val view: View, val navigatorApi: NewNavigatorApi) : RecyclerView.ViewHolder(view) {
    fun bindView(tag : ObservedTagResponse) {
        view.tagsTextView.text = tag.tag
        view.setOnClickListener {
            navigatorApi.openTagActivity(tag.tag.removePrefix("#"))
        }
    }
}