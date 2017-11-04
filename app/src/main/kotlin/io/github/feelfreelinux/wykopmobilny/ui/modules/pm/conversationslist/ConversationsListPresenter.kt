package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class ConversationsListPresenter(private val subscriptionHelperApi: SubscriptionHelperApi, val pmApi: PMApi) : BasePresenter<ConversationsListView>() {
    fun loadConversations() {
        subscriptionHelperApi.subscribe(pmApi.getConversations(), {
            view?.showConversations(it)
        }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelperApi.dispose(this)
    }
}