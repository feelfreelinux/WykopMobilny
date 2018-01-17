package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.UpvoterViewHolder
import javax.inject.Inject

class UpvoterListAdapter @Inject constructor() : RecyclerView.Adapter<UpvoterViewHolder>() {
    val dataset = ArrayList<Upvoter>()

    override fun onBindViewHolder(holder: UpvoterViewHolder?, position: Int) {
        holder?.bindView(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpvoterViewHolder =
        UpvoterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false))
}