package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.EntryResponse
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface BaseFeedView : BaseView {
    fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
}