package io.github.feelfreelinux.wykopmobilny.presenters

import io.github.feelfreelinux.wykopmobilny.objects.Entry
import io.github.feelfreelinux.wykopmobilny.objects.VoteResponse
import io.github.feelfreelinux.wykopmobilny.utils.WykopApiManager

abstract class FeedPresenter(val view : FeedPresenter.View, val wam : WykopApiManager) {
    abstract fun loadData(page : Int)

    interface View {
        fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
    }
}