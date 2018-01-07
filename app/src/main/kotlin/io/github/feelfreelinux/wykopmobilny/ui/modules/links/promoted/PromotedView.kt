package io.github.feelfreelinux.wykopmobilny.ui.modules.links.promoted

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface PromotedView : BaseView {
    fun addDataToAdapter(entryList : List<Link>, shouldClearAdapter : Boolean)
    fun disableLoading()
}