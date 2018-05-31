package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.DownvoterViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.UpvoterViewHolder
import javax.inject.Inject

class DownvoterListAdapter @Inject constructor() : RecyclerView.Adapter<DownvoterViewHolder>() {
    val dataset = ArrayList<Downvoter>()

    override fun onBindViewHolder(holder: DownvoterViewHolder, position: Int) {
        holder.bindView(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownvoterViewHolder =
            DownvoterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.downvoters_list_item, parent, false))
}