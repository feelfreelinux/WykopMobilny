package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed
import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.objects.Entry

interface BaseFeedView : BaseView {
    fun addDataToAdapter(entryList : List<Entry>, shouldClearAdapter : Boolean)
}