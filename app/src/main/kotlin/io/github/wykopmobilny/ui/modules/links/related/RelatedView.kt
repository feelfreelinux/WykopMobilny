package io.github.wykopmobilny.ui.modules.links.related

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Related

interface RelatedView : BaseView {
    fun showRelated(related: List<Related>)
    fun onRefresh()
}
