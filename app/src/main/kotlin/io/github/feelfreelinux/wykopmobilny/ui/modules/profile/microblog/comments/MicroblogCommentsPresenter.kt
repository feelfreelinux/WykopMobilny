package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.microblog.comments

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class MicroblogCommentsPresenter(val schedulers: Schedulers, val profileApi: ProfileApi) : BasePresenter<MicroblogCommentsView>() {
    var page = 1
    lateinit var username : String
    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                profileApi.getEntriesComments(username, page)
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