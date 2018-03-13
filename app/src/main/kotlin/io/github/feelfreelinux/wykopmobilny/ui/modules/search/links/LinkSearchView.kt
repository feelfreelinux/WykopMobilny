package io.github.feelfreelinux.wykopmobilny.ui.modules.search.links

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface LinkSearchView : BaseView {
    fun addDataToAdapter(entryList : List<Link>, shouldClearAdapter : Boolean)
    fun disableLoading()
    var showSearchEmptyView : Boolean
}