package io.github.feelfreelinux.wykopmobilny.callbacks

import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.fragments.TagFeedFragment
import io.github.feelfreelinux.wykopmobilny.fragments.EntryViewFragment
import io.github.feelfreelinux.wykopmobilny.utils.ApiResponseCallback
import io.github.feelfreelinux.wykopmobilny.utils.VoteResponseListener
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

interface FeedClickCallbackInterface {
    fun onVoteClicked(entryId : Int, commentId : Int?, isSelected : Boolean, responseCallback: VoteResponseListener)
    fun onCommentsClicked(entryId : Int)
    fun onProfileClicked(profile : String)
    fun onTagClicked(tag : String)
}

class FeedClickCallbacks(val context : NavigationActivity, val apiManager: WykopApiManager) : FeedClickCallbackInterface {

    override fun onVoteClicked(entryId: Int, commentId: Int?, isSelected: Boolean, responseCallback: VoteResponseListener) {
        if (!isSelected)
            apiManager.voteEntry(entryId, commentId, responseCallback as ApiResponseCallback)
        else
            apiManager.unvoteEntry(entryId, commentId, responseCallback as ApiResponseCallback)
    }

    override fun onCommentsClicked(entryId: Int) {
        context.navActions.openFragment(EntryViewFragment.newInstance(entryId))
    }

    override fun onTagClicked(tag: String) {
        context.navActions.openFragment(TagFeedFragment.newInstance(tag))
    }

    override fun onProfileClicked(login: String) {
        TODO("not implemented")

    }
}