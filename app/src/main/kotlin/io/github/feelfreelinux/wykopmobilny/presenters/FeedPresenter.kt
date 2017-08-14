package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi

abstract class FeedPresenter(val view: View, val wam: WykopApi) {
    abstract fun loadData(page : Int)

    interface View {
        fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
    }
}