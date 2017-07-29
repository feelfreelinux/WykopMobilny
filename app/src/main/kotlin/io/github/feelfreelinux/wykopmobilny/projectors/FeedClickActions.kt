package io.github.feelfreelinux.wykopmobilny.projectors

import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.fragments.TagFeedFragment
import io.github.feelfreelinux.wykopmobilny.adapters.CommentClickListener
import io.github.feelfreelinux.wykopmobilny.adapters.IFeedAdapterActions
import io.github.feelfreelinux.wykopmobilny.adapters.TagClickListener
import io.github.feelfreelinux.wykopmobilny.adapters.VoteClickListener
import io.github.feelfreelinux.wykopmobilny.fragments.EntryViewFragment
import io.github.feelfreelinux.wykopmobilny.utils.printout

class FeedClickActions(val context : NavigationActivity) : IFeedAdapterActions {
    override val entryVoteClickListener: VoteClickListener
        get() = { _, _ -> }
    override val tagClickListener: TagClickListener
        get() = { tag -> viewTagFeed(tag)}
    override val commentClickListener: CommentClickListener
        get() = { id -> viewEntryDetails(id)}

    fun viewTagFeed(tag : String) {
        context.navActions.openFragment(TagFeedFragment.newInstance(context.wam.getData(), tag))
    }

    fun viewEntryDetails(id : Int) {
        printout("GO")
        context.navActions.openFragment(EntryViewFragment.newInstance(context.wam.getData(), id))
    }
}