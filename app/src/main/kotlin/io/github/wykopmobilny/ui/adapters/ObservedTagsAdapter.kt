package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.api.responses.ObservedTagResponse
import io.github.wykopmobilny.databinding.ObservedTagListItemBinding
import io.github.wykopmobilny.ui.adapters.viewholders.ObservedTagViewHolder
import io.github.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.wykopmobilny.utils.layoutInflater
import javax.inject.Inject

class ObservedTagsAdapter @Inject constructor(val navigatorApi: NewNavigatorApi) : RecyclerView.Adapter<ObservedTagViewHolder>() {

    val items = ArrayList<ObservedTagResponse>()

    override fun onBindViewHolder(holder: ObservedTagViewHolder, position: Int) =
        holder.bindView(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObservedTagViewHolder =
        ObservedTagViewHolder(ObservedTagListItemBinding.inflate(parent.layoutInflater, parent, false), navigatorApi)
}
