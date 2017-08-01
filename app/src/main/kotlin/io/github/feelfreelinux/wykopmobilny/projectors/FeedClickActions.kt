package io.github.feelfreelinux.wykopmobilny.projectors

import io.github.feelfreelinux.wykopmobilny.activities.NavigationActivity
import io.github.feelfreelinux.wykopmobilny.adapters.*
import io.github.feelfreelinux.wykopmobilny.fragments.TagFeedFragment
import io.github.feelfreelinux.wykopmobilny.fragments.EntryViewFragment
import io.github.feelfreelinux.wykopmobilny.utils.printout

class FeedClickActions(val context : NavigationActivity) : IFeedAdapterActions, IEntryDetailsActions {
    override val tagClickListener: TagClickListener
        get() = { tag -> viewTagFeed(tag)}
    override val commentClickListener: CommentClickListener
        get() = { id -> viewEntryDetails(id)}

    fun viewTagFeed(tag : String) {
        context.navActions.openFragment(TagFeedFragment.newInstance(tag))
    }

    fun viewEntryDetails(id : Int) {
        printout("GO")
        context.navActions.openFragment(EntryViewFragment.newInstance(id))
    }
}