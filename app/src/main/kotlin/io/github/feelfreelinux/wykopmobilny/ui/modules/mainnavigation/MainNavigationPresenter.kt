package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainNavigationPresenter(private val schedulers: Schedulers,
                              private val notificationsApi: NotificationsApi,
                              private val userManagerApi: UserManagerApi) : BasePresenter<MainNavigationView>() {
    fun startListeningForNotifications() {
        compositeObservable.clear()
        compositeObservable.dispose()
        compositeObservable = CompositeDisposable()
        compositeObservable.apply {
            if (userManagerApi.isUserAuthorized()) {
                add(
                        Observable.interval(0, 5, TimeUnit.MINUTES)
                                .map { notificationsApi.getHashTagNotificationCount() }
                                .subscribe {
                                    add(
                                            it.
                                                    subscribeOn(schedulers.backgroundThread())
                                                    .observeOn(schedulers.mainThread())
                                                    .subscribe({ view?.showHashNotificationsCount(it.count) }, { view?.showErrorDialog(it) })
                                    )
                                }
                )
                add (

                        Observable.interval(0, 5, TimeUnit.MINUTES)
                                .map { notificationsApi.getNotificationCount() }
                                .subscribe {
                                    add(
                                            it.
                                                    subscribeOn(schedulers.backgroundThread())
                                                    .observeOn(schedulers.mainThread())
                                                    .subscribe({ view?.showNotificationsCount(it.count) }, { view?.showErrorDialog(it) })
                                    )
                                }
                )
            }
        }
    }
}