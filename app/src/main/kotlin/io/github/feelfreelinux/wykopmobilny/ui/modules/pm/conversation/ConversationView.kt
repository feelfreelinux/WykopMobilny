package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.FullConversation

interface ConversationView : BaseView {
    fun showConversation(conversation : FullConversation)
    fun hideInputToolbar()
    fun resetInputbarState()
    fun hideInputbarProgress()
}