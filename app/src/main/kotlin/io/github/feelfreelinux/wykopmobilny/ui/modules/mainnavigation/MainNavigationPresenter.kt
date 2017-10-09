package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.feed.hot.HotFragment
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class MainNavigationPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val userManager: UserManagerApi, private val myWykopApi: MyWykopApi) : BasePresenter<MainNavigationView>() {

    fun navigationItemClicked(itemId: Int) {
        when (itemId) {
            R.id.nav_mikroblog -> view?.openFragment(HotFragment.newInstance())
            R.id.login -> { view?.openLoginActivity() }
            R.id.logout -> {
                userManager.logoutUser()
                view?.restartActivity()
            }
        }
    }

    override fun subscribe(view: MainNavigationView) {
        super.subscribe(view)
        setupNavigation()
    }

    private fun setupNavigation() {
        if (userManager.isUserAuthorized()) {
            view?.showUsersMenu(true)
            userManager.getUserCredentials()?.let { view?.avatarUrl = it.avatarUrl }
            getNotificationsCount()
        } else view?.showUsersMenu(false)
    }

    fun getNotificationsCount() {
        myWykopApi.apply {
            subscriptionHelper.subscribe(getNotificationCount(),
                    { view?.notificationCount = it.count },
                    { view?.showErrorDialog(it) },
                    this@MainNavigationPresenter)

            subscriptionHelper.subscribe(getHashTagNotificationCount(), { view?.hashTagNotificationCount = it.count },
                    { view?.showErrorDialog(it) },
                    this@MainNavigationPresenter)
        }
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}