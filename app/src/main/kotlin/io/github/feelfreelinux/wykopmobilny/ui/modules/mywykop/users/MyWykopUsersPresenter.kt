package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.users

import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopView


class MyWykopUsersPresenter(val schedulers: Schedulers, val myWykopApi: MyWykopApi) : BasePresenter<MyWykopView>() {
    var page = 1
    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                myWykopApi.byUsers(page)
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