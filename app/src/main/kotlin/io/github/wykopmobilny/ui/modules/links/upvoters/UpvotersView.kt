package io.github.wykopmobilny.ui.modules.links.upvoters

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Upvoter

interface UpvotersView : BaseView {
    fun showUpvoters(upvoter: List<Upvoter>)
}
