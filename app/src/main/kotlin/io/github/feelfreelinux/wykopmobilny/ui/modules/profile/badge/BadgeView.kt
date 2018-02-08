package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.badge

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.responses.BadgeResponse

interface BadgeView : BaseView {
    fun addDataToAdapter(entryList : List<BadgeResponse>, shouldClearAdapter : Boolean)
    fun disableLoading()
}