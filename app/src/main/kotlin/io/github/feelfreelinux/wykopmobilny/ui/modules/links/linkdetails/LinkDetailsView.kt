package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment

interface LinkDetailsView : BaseView {
    fun showLinkComments(comments: List<LinkComment>)
    fun updateLink(link : Link)
    fun updateLinkComment(comment : LinkComment)
    fun hideInputbarProgress()
    fun resetInputbarState()
    fun hideInputToolbar()
    fun getReplyCommentId() : Int
    fun scrollToComment(id : Int)
}