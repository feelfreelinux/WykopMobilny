package io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments

import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment

interface LinkCommentViewListener {
    fun replyComment(comment: LinkComment)
    fun setCollapsed(comment: LinkComment, isCollapsed: Boolean)
}
