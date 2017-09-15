package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base.BaseVoteButton
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base.BaseVoteButtonPresenter

class EntryVoteButton : BaseVoteButton {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val presenter by lazy { EntryVoteButtonPresenter() }

    fun setEntryData(entryId : Int, voteCount : Int) {
        presenter.entryId = entryId
        text = "+ " + voteCount
    }
}
