package io.github.wykopmobilny.ui.modules.profile.badge

import io.github.wykopmobilny.api.responses.BadgeResponse
import io.github.wykopmobilny.base.BaseView

interface BadgeView : BaseView {
    fun addDataToAdapter(entryList: List<BadgeResponse>, shouldClearAdapter: Boolean)
    fun disableLoading()
}
