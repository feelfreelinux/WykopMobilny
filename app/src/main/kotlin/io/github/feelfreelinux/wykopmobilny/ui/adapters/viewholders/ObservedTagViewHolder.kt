package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import kotlinx.android.synthetic.main.observed_tag_list_item.view.*

class ObservedTagViewHolder(val view: View, val navigatorApi: NewNavigatorApi) : RecyclerView.ViewHolder(view) {
    fun bindView(tag : ObservedTagResponse) {
        view.blockedTextView.text = tag.tag
        view.setOnClickListener {
            navigatorApi.openTagActivity(tag.tag.removePrefix("#"))
        }
    }
}