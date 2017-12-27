package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.github.feelfreelinux.wykopmobilny.ui.modules.Navigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.ConversationLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.EntryLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.TagLinkParser

interface WykopLinkHandlerApi {
    fun handleUrl(context: Activity, url: String, refreshNotifications : Boolean = false)
    fun getLinkIntent(context: Context, url: String): Intent?
}

class WykopLinkHandler(private val navigatorApi: NavigatorApi) : WykopLinkHandlerApi {
    companion object {
        const val PROFILE_PREFIX = '@'
        const val TAG_PREFIX = '#'
        const val ENTRY_MATCHER = "wpis"
        const val TAG_MATCHER = "tag"
        const val PM_MATCHER = "wiadomosc-prywatna"
        const val DELIMITER = "/"
    }

    override fun handleUrl(context: Activity, url: String, refreshNotifications: Boolean) {
        when (url.first()) {
            PROFILE_PREFIX -> handleProfile(url)
            TAG_PREFIX -> handleTag(context, url)
            else -> handleLink(context, url, refreshNotifications)
        }
    }

    private fun handleProfile(login: String) { //@TODO

    }

    private fun handleTag(context: Activity, tag: String) {
        navigatorApi.openTagActivity(context, tag.removePrefix(TAG_PREFIX.toString()))
    }

    private fun handleLink(context: Activity, url: String, refreshNotifications : Boolean) {
        val intent = getLinkIntent(context, url)
        if (intent != null) {
            if (refreshNotifications) context.startActivityForResult(intent, Navigator.STARTED_FROM_NOTIFICATIONS_CODE)
            else context.startActivity(intent)
        } else {
            context.openBrowser(url)
        }
    }

    override fun getLinkIntent(context: Context, url: String): Intent? {
        val resource = url.substringAfter("wykop.pl/")
        return when (resource.substringBefore(DELIMITER)) {
            ENTRY_MATCHER -> {
                EntryActivity.createIntent(context, EntryLinkParser.getEntryId(url)!!, EntryLinkParser.getEntryCommentId(url))
            }
            TAG_MATCHER -> {
                TagActivity.createIntent(context, TagLinkParser.getTag(url))
            }
            PM_MATCHER -> {
                ConversationActivity.createIntent(context, ConversationLinkParser.getConversationUser(url))
            }
            else -> null
        }
    }
}