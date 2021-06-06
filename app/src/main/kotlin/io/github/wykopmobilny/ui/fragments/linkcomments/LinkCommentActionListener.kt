package io.github.wykopmobilny.ui.fragments.linkcomments

import io.github.wykopmobilny.models.dataclass.LinkComment

interface LinkCommentActionListener {
    fun digComment(comment: LinkComment)
    fun buryComment(comment: LinkComment)
    fun removeVote(comment: LinkComment)
    fun deleteComment(comment: LinkComment)
}
