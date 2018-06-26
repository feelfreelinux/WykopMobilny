package io.github.feelfreelinux.wykopmobilny.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.ObservedTagViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import javax.inject.Inject

class ObservedTagsAdapter @Inject constructor(val navigatorApi: NewNavigatorApi) : androidx.recyclerview.widget.RecyclerView.Adapter<ObservedTagViewHolder>() {
    val dataset = ArrayList<ObservedTagResponse>()

    override fun onBindViewHolder(holder: ObservedTagViewHolder, position: Int) {
        holder?.bindView(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservedTagViewHolder =
            ObservedTagViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.observed_tag_list_item, parent, false), navigatorApi)
}