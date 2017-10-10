package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags

import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.NotificationsListView
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class HashTagsNotificationsListPresenter(val subscriptionHelper: SubscriptionHelperApi, val myWykopApi: MyWykopApi) : BasePresenter<NotificationsListView>() {
    var page = 1

    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelper.subscribe(myWykopApi.getHashTagNotifications(page),
                {
                    if (it.isNotEmpty()) {
                        page++
                        view?.addNotifications(it, shouldRefresh)
                    }
                }, { view?.showErrorDialog(it) }, this)
    }

    fun readNotifications() {
        subscriptionHelper.subscribe(myWykopApi.readHashTagNotifications(),
                { view?.showReadToast() }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}