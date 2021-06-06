package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.wykopmobilny.databinding.LinkRelatedListItemBinding
import io.github.wykopmobilny.models.dataclass.Related
import io.github.wykopmobilny.ui.adapters.viewholders.RelatedViewHolder
import io.github.wykopmobilny.ui.widgets.link.related.RelatedWidgetPresenterFactory
import io.github.wykopmobilny.utils.layoutInflater
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class RelatedListAdapter @Inject constructor(
    val userManagerApi: UserManagerApi,
    private val relatedWidgetPresenterFactory: RelatedWidgetPresenterFactory
) : androidx.recyclerview.widget.RecyclerView.Adapter<RelatedViewHolder>() {

    val items = ArrayList<Related>()

    override fun onBindViewHolder(holder: RelatedViewHolder, position: Int) =
        holder.bindView(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedViewHolder =
        RelatedViewHolder(
            LinkRelatedListItemBinding.inflate(parent.layoutInflater, parent, false),
            userManagerApi,
            relatedWidgetPresenterFactory.create()
        )
}
