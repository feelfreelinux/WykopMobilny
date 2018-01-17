package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Author
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.DigResponse

interface LinkView : BaseView {
    fun showVoteCount(digResponse: DigResponse)
    fun showDigged()
    fun showBurried()
    fun showUnvoted()
}