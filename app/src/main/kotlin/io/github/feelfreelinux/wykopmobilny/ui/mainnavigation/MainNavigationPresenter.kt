package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.fragments.HotFeedFragment
import io.github.feelfreelinux.wykopmobilny.utils.ApiPreferences
import io.github.feelfreelinux.wykopmobilny.utils.WykopApi
import io.github.feelfreelinux.wykopmobilny.utils.setupSubscribeIOAndroid
import io.reactivex.disposables.CompositeDisposable

class MainNavigationPresenter(val apiManager : WykopApi, val apiPreferences: ApiPreferences, val view : MainNavigationContract.View) : MainNavigationContract.Presenter {
    override var subscriptions = CompositeDisposable()

    override fun navigationItemClicked(itemId: Int) {
        when (itemId) {
            R.id.nav_mikroblog -> view.openFragment(HotFeedFragment.newInstance())
        }
    }

    override fun setupNavigation() {
        apiPreferences.avatarUrl?.let { view.avatarUrl = it }
        getNotificationsCount()
        view.openFragment(HotFeedFragment.newInstance())
    }

    fun getNotificationsCount() {
        subscriptions.add(
                apiManager.getNotificationCount()
                        .setupSubscribeIOAndroid()
                        .subscribe {
                            (result, _) ->
                            result?.let { view.notificationCount = result.count }
                        })

        subscriptions.add(
                apiManager.getHashTagsNotificationsCount()
                        .setupSubscribeIOAndroid()
                        .subscribe {
                            (result, _) ->
                            result?.let { view.hashTagNotificationCount = result.count }
                        })
    }

}