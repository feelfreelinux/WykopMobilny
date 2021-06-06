package io.github.wykopmobilny.ui.fragments.entries

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Entry
import io.github.wykopmobilny.models.dataclass.Voter

interface EntriesFragmentView : BaseView {
    fun updateEntry(entry: Entry)
    fun showVoters(voters: List<Voter>)
    fun openVotersMenu()
    fun disableLoading()
    fun addItems(items: List<Entry>, shouldRefresh: Boolean = false)
}
