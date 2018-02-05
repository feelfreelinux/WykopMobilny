package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.related

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related

interface ProfileRelatedView : BaseView {
    fun addDataToAdapter(entryList : List<Related>, shouldClearAdapter : Boolean)
    fun disableLoading()
}