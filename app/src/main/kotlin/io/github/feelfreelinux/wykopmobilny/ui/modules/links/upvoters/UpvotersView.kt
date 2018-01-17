package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upvoters

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Upvoter

interface UpvotersView : BaseView {
    fun showUpvoters(upvoter: List<Upvoter>)
}