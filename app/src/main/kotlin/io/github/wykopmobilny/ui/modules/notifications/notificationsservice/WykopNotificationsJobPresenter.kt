package io.github.wykopmobilny.ui.modules.notifications.notificationsservice

import io.github.wykopmobilny.api.notifications.NotificationsApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class WykopNotificationsJobPresenter @Inject constructor(
    val schedulers: Schedulers,
    val notificationsApi: NotificationsApi,
    val userManager: UserManagerApi
) : BasePresenter<WykopNotificationsJobView>() {

    fun checkNotifications() {
        if (userManager.isUserAuthorized()) {
            val notifications = notificationsApi.getNotifications(1)
                .blockingGet()
            val unreadNotifications = notifications.filter { it.new }

            // In this case, we should download full notifications list
            when {
                unreadNotifications.size == notifications.size && unreadNotifications.isNotEmpty() -> getNotificationsCount()
                unreadNotifications.size > 1 -> view?.showNotificationsCount(unreadNotifications.size)
                unreadNotifications.isNotEmpty() -> view?.showNotification(unreadNotifications.first())
                unreadNotifications.isEmpty() -> view?.cancelNotification()
            }
        }
    }

    private fun getNotificationsCount() {
        view?.showNotificationsCount(
            notificationsApi.getNotificationCount()
                .blockingGet().count
        )
    }
}
