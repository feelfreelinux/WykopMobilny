package io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.*


interface LinkCommentsFragmentView : BaseView {
    fun updateComment(comment : LinkComment)
    fun disableLoading()
    fun addItems(items : List<LinkComment>, shouldRefresh : Boolean = false)
}