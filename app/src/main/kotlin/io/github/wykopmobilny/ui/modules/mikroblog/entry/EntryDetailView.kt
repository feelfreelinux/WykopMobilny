package io.github.wykopmobilny.ui.modules.mikroblog.entry

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.Voter

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
