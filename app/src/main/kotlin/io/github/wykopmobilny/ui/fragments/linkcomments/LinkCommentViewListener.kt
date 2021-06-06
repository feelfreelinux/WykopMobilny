package io.github.wykopmobilny.ui.fragments.linkcomments

import io.github.wykopmobilny.models.dataclass.LinkComment

interface LinkCommentViewListener {
    fun replyComment(comment: LinkComment)
    fun setCollapsed(comment: LinkComment, isCollapsed: Boolean)
}
