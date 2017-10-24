package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class ConversationsListPresenter(private val subscriptionHelperApi: SubscriptionHelperApi, val pmApi: PMApi) : BasePresenter<ConversationsListView>() {
    var page = 1

    fun loadConversations(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        subscriptionHelperApi.subscribe(pmApi.getConversations(page), {
            if (it.isNotEmpty()) {
                page ++
                view?.showConversations(it, shouldRefresh)
            } else view?.disableLoading()
        }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelperApi.dispose(this)
    }
}