package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface EntryDetailView : BaseView {
    fun showEntry(entry: Entry)
    fun hideInputbarProgress()
    fun resetInputbarState()
    fun hideInputToolbar()
}