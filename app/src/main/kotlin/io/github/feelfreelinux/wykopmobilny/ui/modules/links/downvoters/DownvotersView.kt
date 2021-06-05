package io.github.feelfreelinux.wykopmobilny.ui.modules.links.downvoters

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Downvoter

interface DownvotersView : BaseView {
    fun showDownvoters(downvoters: List<Downvoter>)
}
