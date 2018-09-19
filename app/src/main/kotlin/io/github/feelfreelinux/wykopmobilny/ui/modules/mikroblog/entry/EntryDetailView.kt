package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter

interface EntryDetailView : BaseView {
    fun showEntry(entry: Entry)
    fun hideInputbarProgress()
    fun resetInputbarState()
    fun hideInputToolbar()
    fun openVotersMenu()
    fun showVoters(voters: List<Voter>)
    fun updateEntry(entry: Entry)
    fun updateComment(comment: EntryComment)
}