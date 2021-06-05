package io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.hashtags

import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Notification
import io.github.feelfreelinux.wykopmobilny.models.dataclass.NotificationHeader
import io.github.feelfreelinux.wykopmobilny.ui.modules.notificationslist.NotificationsListView
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class HashTagsNotificationsListPresenter(
    val schedulers: Schedulers,
    private val notificationsApi: NotificationsApi
) : BasePresenter<NotificationsListView>() {

    var page = 1

    fun loadData(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        notificationsApi.getHashTagNotifications(page)
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
        notificationsApi.readHashTagNotifications()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showReadToast() }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun loadAllNotifications(shouldRefresh: Boolean) {
        notificationsApi.getHashTagNotificationCount()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    if (it.count > 325) {
                        view?.showTooManyNotifications()
                    } else {
                        fetchAllPages(shouldRefresh)
                    }
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    private fun fetchAllPages(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        val allData = arrayListOf<Notification>()
        var dataEmpty = false
        Single.defer { notificationsApi.getHashTagNotifications(page) }
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .repeatUntil { dataEmpty }
            .subscribe(
                {
                    val data = it.filter { it.new }.toMutableList()

                    if (data.isNotEmpty()) {
                        allData.addAll(data)
                        page++
                    } else {
                        dataEmpty = true

                        val sortedData = arrayListOf<Notification>()

                        for (notification in allData.map { it.tag }.toHashSet().toList()) {
                            sortedData.add(NotificationHeader(notification, allData.count { item -> item.tag == notification }))
                            sortedData.addAll(allData.filter { it.tag == notification })
                        }
                        view?.addNotifications(sortedData, true)
                        view?.disableLoading()
                    }
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }
}
