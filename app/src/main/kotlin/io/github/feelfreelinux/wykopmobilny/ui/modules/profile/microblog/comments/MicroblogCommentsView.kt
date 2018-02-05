package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryComment

interface MicroblogCommentsView : BaseView {
    fun addDataToAdapter(entryList : List<EntryComment>, shouldClearAdapter : Boolean)
    fun disableLoading()
}