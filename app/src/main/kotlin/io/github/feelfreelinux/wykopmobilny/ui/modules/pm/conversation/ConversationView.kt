package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.PMMessage

interface ConversationView : BaseView {
    fun showConversation(items : List<PMMessage>)
    fun hideInputToolbar()
    fun resetInputbarState()
    fun hideInputbarProgress()
}