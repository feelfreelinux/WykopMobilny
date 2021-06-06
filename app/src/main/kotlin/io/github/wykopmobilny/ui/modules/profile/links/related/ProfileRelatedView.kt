package io.github.wykopmobilny.ui.modules.profile.links.related

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Related

interface ProfileRelatedView : BaseView {
    fun addDataToAdapter(entryList: List<Related>, shouldClearAdapter: Boolean)
    fun disableLoading()
}
