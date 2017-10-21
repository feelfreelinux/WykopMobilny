package io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview

import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationCountResponse
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class DrawerHeaderPresenter(val subscriptionHelper: SubscriptionHelperApi, val myWykopApi: MyWykopApi) : BasePresenter<DrawerHeaderView>() {
    fun fetchNotifications() {
        val disposable = subscriptionHelper.getSubscriberCompositeDisposable(this)
        disposable.add(
                        Observable.interval(0, 5, TimeUnit.MINUTES)
                                .map { myWykopApi.getHashTagNotificationCount() }
                                .subscribe { getHashTagNotificationsCount(it) }
                )

        disposable.add(
                Observable.interval(0, 5, TimeUnit.MINUTES)
                        .map { myWykopApi.getNotificationCount() }
                        .subscribe { getNotificationsCount(it) }
        )
    }

    fun getNotificationsCount(single: Single<NotificationCountResponse>) {
        subscriptionHelper.subscribe(single,
                { view?.notificationCount = it.count },
                { view?.showErrorDialog(it) },
                this@DrawerHeaderPresenter)
    }

    fun getHashTagNotificationsCount(single: Single<NotificationCountResponse>) {
        subscriptionHelper.subscribe(single,
                { view?.hashTagsNotificationsCount = it.count },
                { view?.showErrorDialog(it) },
                this@DrawerHeaderPresenter)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}