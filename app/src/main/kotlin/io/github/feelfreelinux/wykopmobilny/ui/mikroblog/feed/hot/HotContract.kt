package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedView

interface HotContract {
    interface View : BaseFeedView

    interface Presenter : BaseFeedPresenter, BasePresenter<View> {
        var period : String
    }
}