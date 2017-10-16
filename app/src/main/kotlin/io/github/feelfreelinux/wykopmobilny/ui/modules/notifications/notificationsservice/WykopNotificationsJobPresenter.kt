package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class WykopNotificationsJobPresenter(val subscriptionHelper: SubscriptionHelperApi, val myWykopApi: MyWykopApi, val userManager: UserManagerApi) : BasePresenter<WykopNotificationsJobView>() {
    fun checkNotifications() {
        if (userManager.isUserAuthorized()) {
            subscriptionHelper.subscribe(myWykopApi.getNotifications(1),
                    {
                        val unreadNotifications = it.filter { it.new }

                        // In this case, we should download full notifications list
                        when {
                            unreadNotifications.size == it.size -> getNotificationsCount()
                            unreadNotifications.size > 1 -> view?.showNotificationsCount(unreadNotifications.size)
                            unreadNotifications.isNotEmpty() -> view?.showNotification(unreadNotifications.first())
                        }
                    },
                    { view?.showErrorDialog(it) }, this)
        }
    }

    private fun getNotificationsCount() {
        subscriptionHelper.subscribe(myWykopApi.getNotificationCount(),
                {
                    view?.showNotificationsCount(it.count)
                }, { view?.showErrorDialog(it) }, this)
    }
}