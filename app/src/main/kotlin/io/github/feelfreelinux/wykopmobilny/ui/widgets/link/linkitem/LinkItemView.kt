package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse

interface LinkItemView : BaseView {
    fun showDigged()
    fun showBurried()
    fun showUnvoted()
    fun showVoteCount(digResponse: DigResponse)
}