package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class ProfilePresenter(
    val schedulers: Schedulers,
    val profileApi: ProfileApi
) : BasePresenter<ProfileView>() {

    var userName = "a__s"

    fun loadData() {
        profileApi.getIndex(userName)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showProfile(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun markObserved() {
        profileApi.observe(userName)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun markUnobserved() {
        profileApi.unobserve(userName)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun markBlocked() {
        profileApi.block(userName)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun markUnblocked() {
        profileApi.unblock(userName)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun getBadges() {
        profileApi.getBadges(userName, 0)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showBadges(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}
