package io.github.wykopmobilny.ui.modules.links.downvoters

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Downvoter

interface DownvotersView : BaseView {
    fun showDownvoters(downvoters: List<Downvoter>)
}
