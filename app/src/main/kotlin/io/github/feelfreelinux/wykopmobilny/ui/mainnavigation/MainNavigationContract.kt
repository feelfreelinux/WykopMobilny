package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

interface MainNavigationContract {
    interface View {
        var isLoading : Boolean
        var isRefreshing : Boolean
        var notificationCount : Int
        var hashTagNotificationCount : Int
        var avatarUrl : String

        fun showErrorDialog(e : Throwable)
        fun openFragment(fragment : Fragment)
        fun closeDrawer()
    }

    interface Presenter {
        fun setupNavigation()
        fun navigationItemClicked(itemId : Int)
        var subscriptions : CompositeDisposable
    }
}