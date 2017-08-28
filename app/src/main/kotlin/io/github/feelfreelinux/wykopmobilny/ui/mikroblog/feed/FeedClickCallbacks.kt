package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed

import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry.EntryFragment
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag.TagFragment
import io.github.feelfreelinux.wykopmobilny.utils.VoteResponseListener
import io.github.feelfreelinux.wykopmobilny.api.WykopApi
import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.MainNavigationInterface
import io.github.feelfreelinux.wykopmobilny.utils.textview.EntryTextViewCallbacks

interface FeedClickCallbackInterface : EntryTextViewCallbacks {
    fun onVoteClicked(entryId : Int, commentId : Int?, isSelected : Boolean, responseCallback: VoteResponseListener)
    fun onCommentsClicked(entryId : Int)
}

class FeedClickCallbacks(val context: MainNavigationInterface, private val apiManager: WykopApi) : FeedClickCallbackInterface {

    override fun onVoteClicked(entryId: Int, commentId: Int?, isSelected: Boolean, responseCallback: VoteResponseListener) {
        if (!isSelected)
            apiManager.voteEntry(entryId, commentId) {
                it.fold(
                    { responseCallback.invoke(it) },
                    { context.showErrorDialog(it.exception) }
                )
            }
        else
            apiManager.unvoteEntry(entryId, commentId) {
                it.fold(
                        { responseCallback.invoke(it) },
                        { context.showErrorDialog(it) }
                )
            }
    }

    override fun onCommentsClicked(entryId: Int) {
        context.openFragment(EntryFragment.newInstance(entryId))
    }

    override fun onTagClicked(tag: String) {
        context.openFragment(TagFragment.newInstance(tag))
    }

    override fun onProfileClicked(profile: String) {

    }

    override fun onLinkClicked(link: String) {
        context.openBrowser(link)
    }
}