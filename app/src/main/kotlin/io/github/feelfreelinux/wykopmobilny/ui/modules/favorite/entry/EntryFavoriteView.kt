package io.github.feelfreelinux.wykopmobilny.ui.modules.favorite.entry

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link

interface EntryFavoriteView : BaseView {
    fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
    fun disableLoading()
}