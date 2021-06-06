package io.github.wykopmobilny.ui.modules.pm.conversation

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.FullConversation

interface ConversationView : BaseView {
    fun showConversation(conversation: FullConversation)
    fun hideInputToolbar()
    fun resetInputbarState()
    fun hideInputbarProgress()
}
