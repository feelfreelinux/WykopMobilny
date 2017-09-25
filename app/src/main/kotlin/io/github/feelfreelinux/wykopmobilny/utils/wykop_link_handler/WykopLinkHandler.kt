package io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag.launchTagActivity
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser

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
        context.openBrowser(url)
    }
}