package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.tag

import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedPresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.BaseFeedView

interface TagContract {
    interface View : BaseFeedView
    interface Presenter : BasePresenter<View>, BaseFeedPresenter
}