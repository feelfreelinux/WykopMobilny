package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler

import android.app.Activity
import android.content.Context
import android.content.Intent
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.EntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.getEntryActivityIntent
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.TagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.getTagActivityIntent
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.ConversationActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.getConversationIntent
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.ConversationLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.EntryLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.TagLinkParser

interface WykopLinkHandlerApi {
    fun handleUrl(context: Activity, url : String)
    fun getLinkIntent(context : Context, url : String) : Intent?
}

class WykopLinkHandler : WykopLinkHandlerApi {
    override fun handleUrl(context: Activity, url : String) {
        when(url.first()) {
            '@' -> handleProfile(url)
            '#' -> handleTag(context, url)
            else -> handleLink(context, url)
        }
    }

    private fun handleProfile(login : String) { //@TODO

    }

    private fun handleTag(context: Activity, tag : String) {
        context.startActivity(context.getTagActivityIntent(tag.removePrefix("#")))
    }

    private fun handleLink(context: Activity, url : String) {
        val intent = getLinkIntent(context, url)
        if (intent != null) {
            context.startActivity(intent)
        } else context.openBrowser(url)
    }

    override fun getLinkIntent(context: Context, url : String) : Intent? {
        val resource = url.substringAfter("wykop.pl/")
        return when (resource.substringBefore("/")) {
            "wpis" -> {
                EntryActivity.createIntent(context, EntryLinkParser.getEntryId(url)!!, EntryLinkParser.getEntryCommentId(url))
            }

            "tag" -> {
                TagActivity.createIntent(context, TagLinkParser.getTag(url))
            }

            "wiadomosc-prywatna" -> {
                ConversationActivity.createIntent(context, ConversationLinkParser.getConversationUser(url))
            }

            else -> null

        }
    }
}