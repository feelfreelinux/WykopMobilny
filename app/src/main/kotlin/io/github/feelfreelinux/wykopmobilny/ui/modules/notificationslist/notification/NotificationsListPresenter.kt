package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.notification

import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.NotificationsListView
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class NotificationsListPresenter(val subscriptionHelper: SubscriptionHelperApi, val myWykopApi: MyWykopApi) : BasePresenter<NotificationsListView>() {
    var page = 1

    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelper.subscribe(myWykopApi.getNotifications(page),
                {
                    if (it.isNotEmpty()) {
                        page++
                        view?.addNotifications(it, shouldRefresh)
                    } else view?.disableLoading()
                }, { view?.showErrorDialog(it) }, this)
    }

    fun readNotifications() {
        subscriptionHelper.subscribe(myWykopApi.readNotifications(),
                { view?.showReadToast() }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}