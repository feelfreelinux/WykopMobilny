package io.github.feelfreelinux.wykopmobilny.callbacks

import io.github.feelfreelinux.wykopmobilny.ui.mainnavigation.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.fragments.TagFeedFragment
import io.github.feelfreelinux.wykopmobilny.fragments.EntryViewFragment
import io.github.feelfreelinux.wykopmobilny.utils.VoteResponseListener
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi

interface FeedClickCallbackInterface {
    fun onVoteClicked(entryId : Int, commentId : Int?, isSelected : Boolean, responseCallback: VoteResponseListener)
    fun onCommentsClicked(entryId : Int)
    fun onProfileClicked(profile : String)
    fun onTagClicked(tag : String)
}

class FeedClickCallbacks(val context: NavigationActivity, val apiManager: WykopApi) : FeedClickCallbackInterface {

    override fun onVoteClicked(entryId: Int, commentId: Int?, isSelected: Boolean, responseCallback: VoteResponseListener) {
        if (!isSelected)
            apiManager.voteEntry(entryId, commentId)
        else
            apiManager.unvoteEntry(entryId, commentId)
    }

    override fun onCommentsClicked(entryId: Int) {
        context.openFragment(EntryViewFragment.newInstance(entryId))
    }

    override fun onTagClicked(tag: String) {
        context.openFragment(TagFeedFragment.newInstance(tag))
    }

    override fun onProfileClicked(login: String) {
        TODO("not implemented")

    }
}