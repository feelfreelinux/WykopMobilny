package io.github.feelfreelinux.wykopmobilny.ui.modules.links.hits

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface HitsView : BaseView {
    fun showHits(links : List<Link>)
}