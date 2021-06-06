package io.github.wykopmobilny.ui.modules.mikroblog.feed.hot

import io.github.wykopmobilny.base.BaseView
import io.github.wykopmobilny.models.dataclass.Entry

interface HotView : BaseView {
    fun showHotEntries(entries: List<Entry>, isRefreshing: Boolean)
    fun disableLoading()
}
