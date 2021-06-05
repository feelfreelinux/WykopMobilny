package io.github.feelfreelinux.wykopmobilny.ui.modules.search.users

import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class UsersSearchPresenter(
    val schedulers: Schedulers,
    private val searchApi: SearchApi
) : BasePresenter<UsersSearchView>() {

    fun searchProfiles(q: String) {
        searchApi.searchProfiles(q)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.showUsers(it) },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }
}
