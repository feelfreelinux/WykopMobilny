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
}