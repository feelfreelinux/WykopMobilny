package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.PMMessageViewHolder

class PMMessageAdapter : RecyclerView.Adapter<PMMessageViewHolder>() {
    val messages : ArrayList<PMMessage> = arrayListOf()

    override fun getItemCount(): Int = messages.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PMMessageViewHolder
        = PMMessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.pmmessage_sent_layout, parent, false))

    override fun onBindViewHolder(holder: PMMessageViewHolder, position: Int) =
        holder.bindView(messages[position])

}