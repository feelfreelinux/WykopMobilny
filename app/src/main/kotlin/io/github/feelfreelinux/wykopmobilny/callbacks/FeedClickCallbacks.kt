package io.github.feelfreelinux.wykopmobilny.callbacks

import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.fragments.TagFeedFragment
import io.github.feelfreelinux.wykopmobilny.fragments.EntryViewFragment
import io.github.feelfreelinux.wykopmobilny.presenters.EntryDetailsCallbacks
import io.github.feelfreelinux.wykopmobilny.presenters.FeedViewCallbacks
import io.github.feelfreelinux.wykopmobilny.utils.VoteResponseListener
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

class FeedClickCallbacks(val context : NavigationActivity) : EntryDetailsCallbacks, FeedViewCallbacks {
    val wam = WykopApiManager(context)

    override fun onCommentsClicked(entryId: Int) {
        context.navActions.openFragment(EntryViewFragment.newInstance(entryId))
    }

    override fun onVote(type: String, id: Int, commentId: Int?, responseCallback: VoteResponseListener) {
        wam.voteEntry(id, commentId, responseCallback as (Any) -> Unit)
    }

    override fun onUnvote(type: String, id: Int, commentId: Int?, responseCallback: VoteResponseListener) {
        wam.unvoteEntry(id, commentId, responseCallback as (Any) -> Unit)
    }

    override fun onTagClicked(tag: String) {
        context.navActions.openFragment(TagFeedFragment.newInstance(tag))
    }

    override fun onProfileClicked(login: String) {
        TODO("not implemented")
    }
}