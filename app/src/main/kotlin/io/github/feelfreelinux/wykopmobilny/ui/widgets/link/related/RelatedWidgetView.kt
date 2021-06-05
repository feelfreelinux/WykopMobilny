package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related

import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface RelatedWidgetView : BaseView {
    fun markVoted()
    fun markUnvoted()
    fun setVoteCount(voteCount: Int)
}
