package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.utils.api.getGroupColor
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import kotlinx.android.synthetic.main.conversation_list_item.view.*

class ConversationViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    fun bindView(conversation: Conversation) {
        view.authorAvatarView.setAuthor(conversation.user)
        view.userNameTextView.apply {
            text = conversation.user.nick
            setTextColor(context.getGroupColor(conversation.user.group))
        }
        view.entryDateTextView.text = conversation.lastUpdate
        view.setOnClickListener {
            view.context?.apply {
                view.getActivityContext()!!.startActivity(ConversationActivity.createIntent(view.context, conversation.user.nick))
            }
        }
    }
}