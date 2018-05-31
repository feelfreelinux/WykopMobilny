package io.github.feelfreelinux.wykopmobilny.ui.fragments.entrycomments

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Voter


interface EntryCommentsFragmentView : BaseView {
    fun updateComment(comment : EntryComment)
    fun disableLoading()
    fun showVoters(voters : List<Voter>)
    fun openVotersMenu()
    fun addItems(items : List<EntryComment>, shouldRefresh : Boolean = false)
}