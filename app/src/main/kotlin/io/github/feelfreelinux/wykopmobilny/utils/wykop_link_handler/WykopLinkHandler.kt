package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler

import android.content.Context
import android.content.Intent
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.getEntryActivityIntent
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.getTagActivityIntent
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.getConversationIntent
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.ConversationLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.EntryLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.TagLinkParser

interface WykopLinkHandlerApi {
    fun handleUrl(url : String)
    fun getLinkIntent(url : String) : Intent?
}

class WykopLinkHandler(val context: Context) : WykopLinkHandlerApi {
    override fun handleUrl(url : String) {
        when(url.first()) {
            '@' -> handleProfile(url)
            '#' -> handleTag(url)
            else -> handleLink(url)
        }
    }

    private fun handleProfile(login : String) { //@TODO

    }

    private fun handleTag(tag : String) {
        context.startActivity(context.getTagActivityIntent(tag.removePrefix("#")))
    }

    private fun handleLink(url : String) {
        val intent = getLinkIntent(url)
        if (intent != null) {
            context.startActivity(intent)
        } else context.openBrowser(url)
    }

    override fun getLinkIntent(url : String) : Intent? {
        val resource = url.substringAfter("wykop.pl/")
        return when (resource.substringBefore("/")) {
            "wpis" -> {
                context.getEntryActivityIntent(EntryLinkParser.getEntryId(url)!!, EntryLinkParser.getEntryCommentId(url))
            }

            "tag" -> {
                context.getTagActivityIntent(TagLinkParser.getTag(url))
            }

            "wiadomosc-prywatna" -> {
                context.getConversationIntent(ConversationLinkParser.getConversationUser(url))
            }

            else -> null

        }
    }
}