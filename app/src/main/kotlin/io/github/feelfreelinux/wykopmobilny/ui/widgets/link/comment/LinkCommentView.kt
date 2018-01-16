package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.LinkVoteResponse

interface LinkCommentView : BaseView {
    fun markVotedPlus()
    fun markVotedMinus()
    fun markVoteRemoved()
    fun markCommentAsRemoved()
    fun setVoteCount(voteResponse: LinkVoteResponse)
}