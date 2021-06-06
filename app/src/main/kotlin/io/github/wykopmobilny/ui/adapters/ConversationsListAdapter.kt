package io.github.wykopmobilny.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.wykopmobilny.databinding.ConversationListItemBinding
import io.github.wykopmobilny.models.dataclass.Conversation
import io.github.wykopmobilny.ui.adapters.viewholders.ConversationViewHolder
import io.github.wykopmobilny.utils.layoutInflater

class ConversationsListAdapter : RecyclerView.Adapter<ConversationViewHolder>() {

    val items = ArrayList<Conversation>()

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder =
        ConversationViewHolder(ConversationListItemBinding.inflate(parent.layoutInflater, parent, false))
}
