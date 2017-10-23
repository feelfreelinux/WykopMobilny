package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.getConversationUserFromUrl
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.openConversationActivity
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser

interface WykopNotificationLinkHandlerApi {
    fun handleNotificationLink(type: String, url: String)
}

class WykopNotificationLinkHandler(val context: Context) : WykopNotificationLinkHandlerApi {
    companion object {
        val TYPE_PM = "pm"
    }

    override fun handleNotificationLink(type : String, url : String) {
        when (type) {
            TYPE_PM -> handlePM(url)
            else -> context.openBrowser(url)
        }
    }

    fun handlePM(url: String) {
        context.openConversationActivity(url.getConversationUserFromUrl())
    }
}