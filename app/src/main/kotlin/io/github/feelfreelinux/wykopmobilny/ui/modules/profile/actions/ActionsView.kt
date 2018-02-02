package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface ActionsView : BaseView {
    fun showActions(links: List<EntryLink>)
}