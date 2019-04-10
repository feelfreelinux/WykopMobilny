package io.github.feelfreelinux.wykopmobilny.ui.modules.links.related

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Related

interface RelatedView : BaseView {
    fun showRelated(related: List<Related>)
    fun onRefresh()
}