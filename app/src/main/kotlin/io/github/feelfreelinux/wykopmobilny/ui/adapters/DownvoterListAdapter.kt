package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.DownvoterViewHolder
import javax.inject.Inject

class DownvoterListAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<DownvoterViewHolder>() {

    val items = ArrayList<Downvoter>()

    override fun onBindViewHolder(holder: DownvoterViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownvoterViewHolder =
        DownvoterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.downvoters_list_item, parent, false))
}
