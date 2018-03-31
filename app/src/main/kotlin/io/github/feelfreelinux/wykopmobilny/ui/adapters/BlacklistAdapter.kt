package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.ObservedTagResponse
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.BlacklistViewholder
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.ObservedTagViewHolder
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import javax.inject.Inject

class BlacklistAdapter @Inject constructor() : RecyclerView.Adapter<BlacklistViewholder>() {
    var unblockListener : (String) -> Unit = {}
    val dataset = ArrayList<String>()

    override fun onBindViewHolder(holder: BlacklistViewholder?, position: Int) {
        holder?.bind(dataset[position], unblockListener)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlacklistViewholder =
            BlacklistViewholder(LayoutInflater.from(parent.context).inflate(R.layout.blacklist_blocked_item, parent, false))
}