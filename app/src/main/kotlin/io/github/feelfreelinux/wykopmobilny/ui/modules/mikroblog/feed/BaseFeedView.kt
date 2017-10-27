package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface BaseFeedView : BaseView {
    fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
    fun disableLoading()
}