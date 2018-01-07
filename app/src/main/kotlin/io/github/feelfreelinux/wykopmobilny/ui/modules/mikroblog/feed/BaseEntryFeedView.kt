package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry

interface BaseEntryFeedView : BaseView {
    fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
    fun disableLoading()
}