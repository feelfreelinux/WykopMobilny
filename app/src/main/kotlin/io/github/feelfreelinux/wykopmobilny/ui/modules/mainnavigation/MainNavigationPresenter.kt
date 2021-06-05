package io.github.feelfreelinux.wykopmobilny.ui.modules.mainnavigation

import io.github.feelfreelinux.wykopmobilny.api.notifications.NotificationsApi
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainNavigationPresenter(
    private val schedulers: Schedulers,
    private val notificationsApi: NotificationsApi,
    private val userManagerApi: UserManagerApi,
    private val scraperApi: ScraperApi
) : BasePresenter<MainNavigationView>() {

    private var lastCheckMillis = 0L

    fun startListeningForNotifications() {
        compositeObservable.clear()
        compositeObservable.dispose()
        compositeObservable = CompositeDisposable()
        compositeObservable.apply {
            if (userManagerApi.isUserAuthorized()) {
                add(
                    Observable.interval(0, 1, TimeUnit.MINUTES)
                        .subscribe {
                            checkNotifications(false)
                        }
                )
            }
        }
    }

    fun checkNotifications(shouldForce: Boolean) {
        if (lastCheckMillis.plus(300000L) < (System.currentTimeMillis()) || lastCheckMillis == 0L || shouldForce) {
            lastCheckMillis = System.currentTimeMillis()
            notificationsApi.getNotificationCount().subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.showNotificationsCount(it.count) }, { })
                .intoComposite(compositeObservable)
            notificationsApi.getHashTagNotificationCount().subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.showHashNotificationsCount(it.count) }, { })
                .intoComposite(compositeObservable)
        }
    }

    fun importBlacklist() {
        scraperApi.getBlacklist()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.importBlacklist(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}
