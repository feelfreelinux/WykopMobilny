package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import io.github.feelfreelinux.wykopmobilny.api.embed.ExternalApi
import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainNavigationPresenter(private val schedulers: Schedulers,
                              private val notificationsApi: NotificationsApi,
                              private val userManagerApi: UserManagerApi,
                              private val externalApi: ExternalApi,
                              private val scraperApi : ScraperApi) : BasePresenter<MainNavigationView>() {
    var lastCheckMilis = 0L

    fun startListeningForNotifications() {
        compositeObservable.clear()
        compositeObservable.dispose()
        compositeObservable = CompositeDisposable()
        compositeObservable.apply {
            if (userManagerApi.isUserAuthorized()) {
                add (

                        Observable.interval(0, 1, TimeUnit.MINUTES)
                                .subscribe {
                                    checkNotifications(false)
                                }
                )
            }
        }
    }

    fun checkNotifications(shouldForce: Boolean) {
        if (lastCheckMilis.plus(300000L) < (System.currentTimeMillis()) || lastCheckMilis == 0L || shouldForce) {
            lastCheckMilis = System.currentTimeMillis()
            compositeObservable.add(notificationsApi.getNotificationCount().
                    subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({ view?.showNotificationsCount(it.count) }, { }) )
            compositeObservable.add(notificationsApi.getHashTagNotificationCount().
                    subscribeOn(schedulers.backgroundThread())
                    .observeOn(schedulers.mainThread())
                    .subscribe({ view?.showHashNotificationsCount(it.count) }, {  }) )
        }
    }

    fun checkUpdates() {
        compositeObservable.add(
                externalApi.checkUpdates()
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.checkUpdate(it) }, {})
        )

    }

    fun checkWeeklyUpdates() {
        compositeObservable.add(
                externalApi.checkWeeklyUpdates()
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.checkUpdate(it) }, {})
        )
    }

    fun importBlacklist() {
        compositeObservable.add(
                scraperApi.getBlacklist()
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.importBlacklist(it) }, { view?.showErrorDialog(it) })
        )
    }
}