package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class ActionsFragmentPresenter(val schedulers: Schedulers, val profileApi: ProfileApi) : BasePresenter<ActionsView>() {
    lateinit var username : String
    fun getActions() {
        compositeObservable.add(
            profileApi.getActions(username)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.showActions(it) }, { view?.showErrorDialog(it) })
        )
    }
}