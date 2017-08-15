package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.objects.Entry

interface EntryContract {
    interface View : BaseView {
        fun showEntry(entry : Entry)
    }
    interface Presenter : BasePresenter<View> {
        fun loadData()
    }
}