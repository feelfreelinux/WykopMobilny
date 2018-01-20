package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_related_list_item.view.*

class RelatedViewHolder(val view: View, val userManagerApi: UserManagerApi) : RecyclerView.ViewHolder(view) {
    fun bindView(related: Related) {
        view.relatedView.setRelatedData(related, userManagerApi)
    }
}