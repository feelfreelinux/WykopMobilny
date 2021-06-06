package io.github.wykopmobilny.ui.modules.pm.conversationslist

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Conversation

interface ConversationsListView : BaseView {
    fun showConversations(conversations: List<Conversation>)
}
