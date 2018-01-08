package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigator
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.ConversationLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.EntryLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.TagLinkParser

interface WykopLinkHandlerApi {
    fun handleUrl(url: String, refreshNotifications : Boolean = false)
}

class WykopLinkHandler(val context: Activity, private val navigatorApi: NewNavigatorApi) : WykopLinkHandlerApi {
    companion object {
        const val PROFILE_PREFIX = '@'
        const val TAG_PREFIX = '#'
        const val ENTRY_MATCHER = "wpis"
        const val TAG_MATCHER = "tag"
        const val PM_MATCHER = "wiadomosc-prywatna"
        const val DELIMITER = "/"

        fun getLinkIntent(url: String, context : Context): Intent? {
            val resource = url.substringAfter("wykop.pl/")
            return when (resource.substringBefore(Companion.DELIMITER)) {
                Companion.ENTRY_MATCHER -> {
                    EntryActivity.createIntent(context, EntryLinkParser.getEntryId(url)!!, EntryLinkParser.getEntryCommentId(url))
                }
                Companion.TAG_MATCHER -> {
                    TagActivity.createIntent(context, TagLinkParser.getTag(url))
                }
                Companion.PM_MATCHER -> {
                    ConversationActivity.createIntent(context, ConversationLinkParser.getConversationUser(url))
                }
                else -> null
            }
        }
    }

    override fun handleUrl(url: String, refreshNotifications: Boolean) {
        when (url.first()) {
            PROFILE_PREFIX -> handleProfile(url)
            TAG_PREFIX -> handleTag(url)
            else -> handleLink(url, refreshNotifications)
        }
    }

    private fun handleProfile(login: String) { //@TODO

    }

    private fun handleTag(tag: String) {
        navigatorApi.openTagActivity(tag.removePrefix(TAG_PREFIX.toString()))
    }

    private fun handleLink(url: String, refreshNotifications : Boolean) {
        val intent = getLinkIntent(url, context)
        if (intent != null) {
            if (refreshNotifications) context.startActivityForResult(intent, NewNavigator.STARTED_FROM_NOTIFICATIONS_CODE)
            else context.startActivity(intent)
        } else {
            navigatorApi.openBrowser(url)
        }
    }
}