package io.github.feelfreelinux.wykopmobilny.ui.modules.profile

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class ProfilePresenter(val schedulers: Schedulers, val profileApi: ProfileApi) : BasePresenter<ProfileView>() {
    var userName = "a__s"
    fun loadData() {
        compositeObservable.add(
                profileApi.getIndex(userName)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showProfile(it) }, { view?.showErrorDialog(it) })
        )
    }

    fun markObserved() {
        compositeObservable.add(
                profileApi.observe(userName)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })

        )
    }

    fun markUnobserved() {
        compositeObservable.add(
                profileApi.unobserve(userName)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })

        )
    }

    fun markBlocked() {
        compositeObservable.add(
                profileApi.block(userName)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })

        )
    }

    fun markUnblocked() {
        compositeObservable.add(
                profileApi.unblock(userName)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showButtons(it) }, { view?.showErrorDialog(it) })

        )
    }
}