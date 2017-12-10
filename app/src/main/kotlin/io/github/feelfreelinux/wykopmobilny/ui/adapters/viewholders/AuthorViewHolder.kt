package io.github.feelfreelinux.wykopmobilny.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.utils.getActivityContext
import kotlinx.android.synthetic.main.conversation_list_item.view.*

class AuthorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bindView(author : Author) {
        view.authorHeaderView.setAuthorData(author, "")
    }
}