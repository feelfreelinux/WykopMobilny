package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry

import android.content.Context
import android.util.AttributeSet
import io.github.feelfreelinux.wykopmobilny.WykopApp
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButton
import javax.inject.Inject

class EntryVoteButton : BaseVoteButton, EntryVoteButtonView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @Inject lateinit var presenter : EntryVoteButtonPresenter

    override fun unvote() {
        presenter.unvote()
    }

    override fun vote() {
        presenter.vote()
    }

    fun setEntryData(entryId : Int, entryVotes : Int) {
        WykopApp.uiInjector.inject(this)
        presenter.subscribe(this)
        presenter.entryId = entryId
        voteCount = entryVotes
    }
}
