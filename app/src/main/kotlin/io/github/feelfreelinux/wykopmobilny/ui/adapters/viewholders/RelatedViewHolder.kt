package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related.RelatedWidgetPresenter
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import kotlinx.android.synthetic.main.link_related_list_item.view.*

class RelatedViewHolder(val view: View, val userManagerApi: UserManagerApi, val relatedWidgetPresenter: RelatedWidgetPresenter) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    fun bindView(related: Related) {
        view.relatedView.setRelatedData(related, userManagerApi, relatedWidgetPresenter)
    }
}