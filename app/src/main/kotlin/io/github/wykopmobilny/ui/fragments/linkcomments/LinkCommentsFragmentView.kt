package io.github.wykopmobilny.ui.fragments.linkcomments

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.LinkComment

interface LinkCommentsFragmentView : BaseView {
    fun updateComment(comment: LinkComment)
    fun disableLoading()
    fun addItems(items: List<LinkComment>, shouldRefresh: Boolean = false)
}
