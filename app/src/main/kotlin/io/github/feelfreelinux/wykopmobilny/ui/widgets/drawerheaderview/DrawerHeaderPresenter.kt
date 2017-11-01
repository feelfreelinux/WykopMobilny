package io.github.feelfreelinux.wykopmobilny.ui.widgets.drawerheaderview

import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.models.pojo.NotificationCountResponse
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

class DrawerHeaderPresenter(val subscriptionHelper: SubscriptionHelperApi, val myWykopApi: MyWykopApi) : BasePresenter<DrawerHeaderView>() {
    fun fetchNotifications() {
        subscriptionHelper.dispose(this)
        val disposable = subscriptionHelper.getSubscriberCompositeDisposable(this)
        disposable.add(
                        Observable.interval(0, 5, TimeUnit.MINUTES)
                                .map { zipNotificationSingle() }
                                .subscribe { getNotificationsCount(it) }
                )
    }

    fun zipNotificationSingle() : Single<Pair<NotificationCountResponse, NotificationCountResponse>> {
        return Single.zip(
                myWykopApi.getNotificationCount(),
                myWykopApi.getHashTagNotificationCount(),
                BiFunction { t1, t2 -> Pair(t1, t2) })

    }

    fun getNotificationsCount(single: Single<Pair<NotificationCountResponse, NotificationCountResponse>>) {
        subscriptionHelper.subscribe(single,
                {
                    view?.notificationCount = it.first.count
                    view?.hashTagsNotificationsCount = it.second.count
                },
                { view?.showErrorDialog(it) },
                this@DrawerHeaderPresenter)
    }


    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}