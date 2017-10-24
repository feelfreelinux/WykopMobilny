package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry.openEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.tag.launchTagActivity
import io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation.openConversationActivity
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.printout
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.ConversationLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.EntryLinkParser
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.linkparser.TagLinkParser

interface WykopLinkHandlerApi {
    fun handleUrl(url : String)
}

class WykopLinkHandler(val context: Context) : WykopLinkHandlerApi {
    override fun handleUrl(url : String) {
        when(url.first()) {
            '@' -> handleProfile(url)
            '#' -> handleTag(url)
            else -> handleLink(url)
        }
    }

    private fun handleProfile(login : String) {

    }

    private fun handleTag(tag : String) {
        context.launchTagActivity(tag.removePrefix("#"))
    }

    private fun handleLink(url : String) {
        if (url.contains("://www.wykop.pl/") || url.contains("://wykop.pl/")) {
            val resource = url.substringAfter("wykop.pl/")
            printout(resource.substringBefore("/"))
            when (resource.substringBefore("/")) {
                "wpis" -> {
                    context.openEntryActivity(EntryLinkParser.getEntryId(url)!!, EntryLinkParser.getEntryCommentId(url))
                }

                "tag" -> {
                    context.launchTagActivity(TagLinkParser.getTag(url))
                }

                "wiadomosc-prywatna" -> {
                    context.openConversationActivity(ConversationLinkParser.getConversationUser(url))
                }

                else -> context.openBrowser(url)
            }
        } else context.openBrowser(url)
    }
}