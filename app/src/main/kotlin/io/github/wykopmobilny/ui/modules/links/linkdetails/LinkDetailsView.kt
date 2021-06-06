package io.github.wykopmobilny.ui.modules.links.linkdetails

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.models.dataclass.LinkComment

interface LinkDetailsView : BaseView {
    fun showLinkComments(comments: List<LinkComment>)
    fun updateLink(link: Link)
    fun updateLinkComment(comment: LinkComment)
    fun hideInputbarProgress()
    fun resetInputbarState()
    fun hideInputToolbar()
    fun getReplyCommentId(): Int
    fun scrollToComment(id: Int)
}
