package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment

interface LinkDetailsView : BaseView {
    fun showLinkComments(comments: List<LinkComment>)
}