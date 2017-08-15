package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import android.support.v4.app.Fragment
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.BaseView

interface MainNavigationContract {
    interface View : BaseView {
        var isLoading : Boolean
        var isRefreshing : Boolean
        var notificationCount : Int
        var hashTagNotificationCount : Int
        var avatarUrl : String

        fun openFragment(fragment : Fragment)
        fun closeDrawer()
    }

    interface Presenter : BasePresenter<View> {
        fun getNotificationsCount()
        fun navigationItemClicked(itemId : Int)
    }
}