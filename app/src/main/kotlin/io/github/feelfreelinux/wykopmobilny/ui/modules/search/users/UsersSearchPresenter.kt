package io.github.feelfreelinux.wykopmobilny.ui.modules.search.users

import io.github.feelfreelinux.wykopmobilny.api.search.SearchApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class UsersSearchPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val searchApi: SearchApi) : BasePresenter<UsersSearchView>() {
    fun searchProfiles(q : String) {
        subscriptionHelperApi.subscribe(searchApi.searchProfiles(q),
                { view?.showUsers(it) },
                { view?.showErrorDialog(it) }, this)
    }
}