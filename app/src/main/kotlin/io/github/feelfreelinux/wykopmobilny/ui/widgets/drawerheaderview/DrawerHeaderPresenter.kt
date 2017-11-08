package io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview

import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.NotificationsCountResponse
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class DrawerHeaderPresenter(val subscriptionHelper: SubscriptionHelperApi,
                            private val notificationsApi: NotificationsApi) : BasePresenter<DrawerHeaderView>() {
    private val intervalDisposable = CompositeDisposable()

    fun fetchNotifications() {
        intervalDisposable.clear()
        intervalDisposable.add(
                Observable.interval(0, 5, TimeUnit.MINUTES)
                        .map { notificationsApi.getHashTagNotificationCount() }
                        .subscribe { getHashTagNotificationsCount(it) }
        )

        intervalDisposable.add(
                Observable.interval(0, 5, TimeUnit.MINUTES)
                        .map { notificationsApi.getNotificationCount() }
                        .subscribe { getNotificationsCount(it) }
        )
    }

    private fun getNotificationsCount(single: Single<NotificationsCountResponse>) {
        subscriptionHelper.subscribe(single,
                { view?.notificationCount = it.count },
                { view?.showErrorDialog(it) },
                this@DrawerHeaderPresenter)
    }

    private fun getHashTagNotificationsCount(single: Single<NotificationsCountResponse>) {
        subscriptionHelper.subscribe(single,
                { view?.hashTagsNotificationsCount = it.count },
                { view?.showErrorDialog(it) },
                this@DrawerHeaderPresenter)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
        intervalDisposable.dispose()
        intervalDisposable.clear()
    }
}
