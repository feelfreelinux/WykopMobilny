package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.wykopmobilny.base.adapter.EndlessProgressAdapter
import io.github.wykopmobilny.databinding.LinkRelatedListItemBinding
import io.github.wykopmobilny.models.dataclass.Related
import io.github.wykopmobilny.ui.adapters.viewholders.RelatedViewHolder
import io.github.wykopmobilny.ui.widgets.link.related.RelatedWidgetPresenterFactory
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
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
            LinkRelatedListItemBinding.inflate(parent.layoutInflater, parent, false),
            userManagerApi,
            relatedWidgetPresenterFactory.create()
        )
}
