package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index

import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.MyWykopView
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class MyWykopIndexPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val myWykopApi: MyWykopApi) : BasePresenter<MyWykopView>() {
    var page = 1
    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelperApi.subscribe(myWykopApi.getIndex(page),
                {
                    if (it.isNotEmpty()) {
                        page++
                        view?.addDataToAdapter(it, shouldRefresh)
                    } else view?.disableLoading()
                },
                { view?.showErrorDialog(it) }, this)
    }
}