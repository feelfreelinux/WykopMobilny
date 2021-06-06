package io.github.wykopmobilny.ui.widgets.link.related

import io.github.wykopmobilny.base.BaseView

interface RelatedWidgetView : BaseView {
    fun markVoted()
    fun markUnvoted()
    fun setVoteCount(voteCount: Int)
}
