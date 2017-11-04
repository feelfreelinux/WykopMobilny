package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import io.github.feelfreelinux.wykopmobilny.R
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class MainNavigationPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val userManager: UserManagerApi, private val notificationsApi: NotificationsApi) : BasePresenter<MainNavigationView>() {

    fun navigationItemClicked(itemId: Int) {
        when (itemId) {
            R.id.logout -> {
                userManager.logoutUser()
                view?.restartActivity()
            }
            else -> view?.showNotImplementedToast()
        }
    }

    override fun subscribe(view: MainNavigationView) {
        super.subscribe(view)
        setupNavigation()
    }

    private fun setupNavigation() {
        view?.showUsersMenu(userManager.isUserAuthorized())
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}