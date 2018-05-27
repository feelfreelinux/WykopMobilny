package io.github.feelfreelinux.wykopmobilny.ui.fragments.entrylink

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter


interface EntryLinkFragmentView : BaseView {
    fun updateEntry(entry : Entry)
    fun updateLink(link : Link)
    fun showVoters(voters : List<Voter>)
    fun openVotersMenu()
    fun disableLoading()
    fun addItems(items : List<EntryLink>, shouldRefresh : Boolean = false)
}