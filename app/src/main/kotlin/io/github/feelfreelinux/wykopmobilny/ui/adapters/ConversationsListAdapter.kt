package io.github.feelfreelinux.wykopmobilny.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.base.BaseProgressAdapter
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders.ConversationViewHolder

class ConversationsListAdapter : BaseProgressAdapter<ConversationViewHolder, Conversation>() {
    override fun createViewHolder(parent: ViewGroup)
            = ConversationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.conversation_list_item, parent, false))

    override fun bindHolder(holder: ConversationViewHolder, position: Int) {
        holder.bindView(data[position])
    }

}