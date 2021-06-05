package io.github.feelfreelinux.wykopmobilny.ui.fragments.entries

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter

interface EntriesFragmentView : BaseView {
    fun updateEntry(entry: Entry)
    fun showVoters(voters: List<Voter>)
    fun openVotersMenu()
    fun disableLoading()
    fun addItems(items: List<Entry>, shouldRefresh: Boolean = false)
}
