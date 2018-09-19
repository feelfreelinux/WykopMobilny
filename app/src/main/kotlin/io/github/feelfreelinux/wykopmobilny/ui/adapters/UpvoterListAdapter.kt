package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.UpvoterViewHolder
import javax.inject.Inject

class UpvoterListAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<UpvoterViewHolder>() {

    val items = ArrayList<Upvoter>()

    override fun onBindViewHolder(holder: UpvoterViewHolder, position: Int) =
        holder.bindView(items[position])

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpvoterViewHolder =
        UpvoterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false))
}