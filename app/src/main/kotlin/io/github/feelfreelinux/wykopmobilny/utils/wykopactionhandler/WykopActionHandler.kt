package io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.openEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag.launchTagActivity
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.textview.EntryTextViewCallbacks

interface WykopActionHandler : EntryTextViewCallbacks {
    fun onCommentsClicked(entryId : Int)
}

class WykopActionHandlerImpl(private val context: Context) : WykopActionHandler {
    override fun onCommentsClicked(entryId: Int) {
        context.openEntryActivity(entryId)
    }

    override fun onTagClicked(tag: String) {
        context.launchTagActivity(tag)
    }

    override fun onProfileClicked(profile: String) {

    }

    override fun onLinkClicked(link: String) {
        context.openBrowser(link)
    }
}