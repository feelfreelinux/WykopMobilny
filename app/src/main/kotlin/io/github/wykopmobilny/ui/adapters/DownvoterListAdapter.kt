package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import io.github.wykopmobilny.databinding.DownvotersListItemBinding
import io.github.wykopmobilny.models.dataclass.Downvoter
import io.github.wykopmobilny.ui.adapters.viewholders.DownvoterViewHolder
import io.github.wykopmobilny.utils.layoutInflater
import javax.inject.Inject

class DownvoterListAdapter @Inject constructor() : androidx.recyclerview.widget.RecyclerView.Adapter<DownvoterViewHolder>() {

    val items = ArrayList<Downvoter>()

    override fun onBindViewHolder(holder: DownvoterViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownvoterViewHolder =
        DownvoterViewHolder(DownvotersListItemBinding.inflate(parent.layoutInflater, parent, false))
}
