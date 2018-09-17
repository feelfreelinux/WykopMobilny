package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RecyclableViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RelatedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related.RelatedWidgetPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class ProfileRelatedAdapter @Inject constructor(
    val userManagerApi: UserManagerApi,
    private val relatedWidgetPresenterFactory: RelatedWidgetPresenterFactory
) : EndlessProgressAdapter<RelatedViewHolder, Related>() {

    override fun bindHolder(holder: RelatedViewHolder, position: Int) =
        holder.bindView(data[position])

    override fun getViewType(position: Int) = 2137

    override fun constructViewHolder(parent: ViewGroup, viewType: Int): RelatedViewHolder =
        RelatedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.link_related_list_item, parent, false),
            userManagerApi,
            relatedWidgetPresenterFactory.create()
        )

    override fun onViewRecycled(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        (holder as? RecyclableViewHolder)?.cleanRecycled()
        super.onViewRecycled(holder)
    }
}