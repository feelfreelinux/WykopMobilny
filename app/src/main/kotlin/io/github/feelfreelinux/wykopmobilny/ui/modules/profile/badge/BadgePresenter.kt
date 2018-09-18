package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.badge

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class BadgePresenter(
    val schedulers: Schedulers,
    val profileApi: ProfileApi
) : BasePresenter<BadgeView>() {

    var page = 1
    lateinit var username: String

    fun loadData(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
            profileApi.getBadges(username, page)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe(
                    {
                        if (it.isNotEmpty()) {
                            page++
                            view?.addDataToAdapter(it, shouldRefresh)
                        } else view?.disableLoading()
                    },
                    { view?.showErrorDialog(it) }
                )
        )
    }
}