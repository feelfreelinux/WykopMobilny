package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment

interface ProfileLinkCommentsView : BaseView {
    fun addDataToAdapter(entryList : List<LinkComment>, shouldClearAdapter : Boolean)
    fun disableLoading()
}