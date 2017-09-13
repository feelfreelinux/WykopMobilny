package io.github.feelfreelinux.wykopmobilny.utils.wykopactionhandler

import android.content.Context
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.base.BaseActivity
import io.github.feelfreelinux.wykopmobilny.ui.elements.dialogs.showExceptionDialog
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.openEntryActivity
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag.launchTagActivity
import io.github.feelfreelinux.wykopmobilny.utils.VoteResponseListener
import io.github.feelfreelinux.wykopmobilny.utils.openBrowser
import io.github.feelfreelinux.wykopmobilny.utils.textview.EntryTextViewCallbacks

interface WykopActionHandler : EntryTextViewCallbacks {
    fun onVoteClicked(entryId : Int, commentId : Int?, isSelected : Boolean, responseCallback: VoteResponseListener)
    fun onCommentsClicked(entryId : Int)
}

class WykopActionHandlerImpl(private val context: Context, private val apiManager: WykopApi) : WykopActionHandler {
    override fun onVoteClicked(entryId: Int, commentId: Int?, isSelected: Boolean, responseCallback: VoteResponseListener) {
        if (!isSelected)
            apiManager.voteEntry(entryId, commentId) {
                it.fold(
                        { responseCallback.invoke(it) },
                        { context.showExceptionDialog(it.exception) }
                )
            }
        else
            apiManager.unvoteEntry(entryId, commentId) {
                it.fold(
                        { responseCallback.invoke(it) },
                        { context.showExceptionDialog(it) }
                )
            }
    }

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