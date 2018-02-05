package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.digged

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment

interface DiggedLinksView : BaseView {
    fun addDataToAdapter(entryList : List<Link>, shouldClearAdapter : Boolean)
    fun disableLoading()
}