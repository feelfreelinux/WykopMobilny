package io.github.feelfreelinux.wykopmobilny.ui.fragments.links

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface LinksFragmentView : BaseView {
    fun updateLink(link : Link)
    fun disableLoading()
    fun addItems(items : List<Link>, shouldRefresh : Boolean = false)
}