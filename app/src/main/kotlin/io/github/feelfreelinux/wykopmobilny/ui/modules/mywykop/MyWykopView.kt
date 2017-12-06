package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop

import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.EntryLink

interface MyWykopView : BaseView {
    fun addDataToAdapter(entryList : List<EntryLink>, shouldClearAdapter : Boolean)
    fun disableLoading()
}