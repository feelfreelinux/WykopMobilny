package io.github.feelfreelinux.wykopmobilny.ui.mainnavigation

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.utils.api.CredentialsPreferencesApi
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class MainNavigationPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val apiPreferences: CredentialsPreferencesApi, private val myWykopApi: MyWykopApi) : BasePresenter<MainNavigationView>() {

    fun navigationItemClicked(itemId: Int) {
        when (itemId) {
            R.id.nav_mikroblog -> view?.openFragment(HotFragment.newInstance())
            R.id.login -> view?.openLoginActivity()
            R.id.logout -> logoutUser()
        }
    }

    private fun logoutUser() {
        apiPreferences.logoutUser()
        view?.restartActivity()
    }

    override fun subscribe(view: MainNavigationView) {
        super.subscribe(view)
        setupNavigation()
    }

    private fun setupNavigation() {
        if (apiPreferences.isUserAuthorized()) {
            view?.showUsersMenu(true)
            apiPreferences.avatarUrl?.let { view?.avatarUrl = it }
            getNotificationsCount()
        } else view?.showUsersMenu(false)
    }

    fun getNotificationsCount() {
        myWykopApi.apply {
            subscriptions.add(
                    subscriptionHelper.subscribeOnSchedulers(getNotificationCount())
                            .subscribe({ view?.notificationCount = it.count }, { view?.showErrorDialog(it) }))

            subscriptions.add(
                    subscriptionHelper.subscribeOnSchedulers(getHashTagNotificationCount())
                            .subscribe({ view?.hashTagNotificationCount = it.count }, { view?.showErrorDialog(it) }))
        }
    }

}