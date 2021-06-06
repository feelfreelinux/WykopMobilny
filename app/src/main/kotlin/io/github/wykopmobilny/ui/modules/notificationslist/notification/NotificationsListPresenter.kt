package io.github.wykopmobilny.ui.modules.notificationslist.notification

import io.github.wykopmobilny.api.notifications.NotificationsApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.notificationslist.NotificationsListView
import io.github.wykopmobilny.utils.intoComposite

class NotificationsListPresenter(
    val schedulers: Schedulers,
    val notificationsApi: NotificationsApi
) : BasePresenter<NotificationsListView>() {

    var page = 1

    fun loadData(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        notificationsApi.getNotifications(page)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    if (it.isNotEmpty()) {
                        page++
                        view?.addNotifications(it, shouldRefresh)
                    } else view?.disableLoading()
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    fun readNotifications() {
        notificationsApi.readNotifications()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showReadToast() }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}
