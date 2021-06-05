package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation

interface ConversationsListView : BaseView {
    fun showConversations(conversations: List<Conversation>)
}
