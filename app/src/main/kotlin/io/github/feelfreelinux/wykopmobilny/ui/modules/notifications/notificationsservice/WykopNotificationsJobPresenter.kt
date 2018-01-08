package io.github.feelfreelinux.wykopmobilny.ui.modules.notifications.notificationsservice

import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import javax.inject.Inject

class WykopNotificationsJobPresenter @Inject constructor(val schedulers: Schedulers, val notificationsApi: NotificationsApi, val userManager: UserManagerApi) : BasePresenter<WykopNotificationsJobView>() {
    fun checkNotifications() {
        if (userManager.isUserAuthorized()) {
            compositeObservable.add(
                    notificationsApi.getNotifications(1)
                            .subscribeOn(schedulers.backgroundThread())
                            .observeOn(schedulers.mainThread())
                            .subscribe(
                                    {
                                        val unreadNotifications = it.filter { it.new }

                                        // In this case, we should download full notifications list
                                        when {
                                            unreadNotifications.size == it.size && unreadNotifications.isNotEmpty() -> getNotificationsCount()
                                            unreadNotifications.size > 1 -> view?.showNotificationsCount(unreadNotifications.size)
                                            unreadNotifications.isNotEmpty() -> view?.showNotification(unreadNotifications.first())
                                            unreadNotifications.isEmpty() -> view?.cancelNotification()
                                        }
                                    },
                                    { view?.showErrorDialog(it) }
                            )
            )
        }
    }

    private fun getNotificationsCount() {
        compositeObservable.add(
                notificationsApi.getNotificationCount()
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.showNotificationsCount(it.count) }, { view?.showErrorDialog(it) })
        )
    }
}