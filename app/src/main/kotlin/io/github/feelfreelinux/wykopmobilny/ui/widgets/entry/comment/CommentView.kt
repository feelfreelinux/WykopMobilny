package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry.comment

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter

interface CommentView : BaseView {
    fun markCommentAsRemoved()
    fun markCommentVoted(voteCount : Int)
    fun markCommentUnvoted(voteCount: Int)
    fun showVoters(voters : List<Voter>)
}