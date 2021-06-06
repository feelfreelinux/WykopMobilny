package io.github.wykopmobilny.ui.fragments.entrycomments

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.EntryComment
import io.github.wykopmobilny.models.dataclass.Voter

interface EntryCommentsFragmentView : BaseView {
    fun updateComment(comment: EntryComment)
    fun disableLoading()
    fun showVoters(voters: List<Voter>)
    fun openVotersMenu()
    fun addItems(items: List<EntryComment>, shouldRefresh: Boolean = false)
}
