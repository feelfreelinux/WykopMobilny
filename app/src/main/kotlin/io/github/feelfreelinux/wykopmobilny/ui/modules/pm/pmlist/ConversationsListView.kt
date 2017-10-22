package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.pmlist

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Conversation

interface ConversationsListView : BaseView {
    fun showConversations(items : List<Conversation>, shouldCleanAdapter : Boolean)
}