package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.ConversationViewHolder

class ConversationsListAdapter : RecyclerView.Adapter<ConversationViewHolder>() {
    val dataset = ArrayList<Conversation>()

    override fun onBindViewHolder(holder: ConversationViewHolder?, position: Int) {
        holder?.bindView(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder =
        ConversationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false))
}