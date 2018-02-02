package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.entries

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry

interface MicroblogEntriesView : BaseView {
    fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
    fun disableLoading()
}