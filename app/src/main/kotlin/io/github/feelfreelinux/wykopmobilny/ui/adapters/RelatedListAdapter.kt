package io.github.feelfreelinux.wykopmobilny.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.RelatedViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related.RelatedWidgetPresenterFactory
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class RelatedListAdapter @Inject constructor(val userManagerApi: UserManagerApi, val relatedWidgetPresenterFactory: RelatedWidgetPresenterFactory) : androidx.recyclerview.widget.RecyclerView.Adapter<RelatedViewHolder>() {
    val dataset = ArrayList<Related>()

    override fun onBindViewHolder(holder: RelatedViewHolder, position: Int) {
        holder?.bindView(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedViewHolder =
        RelatedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.link_related_list_item, parent, false), userManagerApi, relatedWidgetPresenterFactory.create())
}