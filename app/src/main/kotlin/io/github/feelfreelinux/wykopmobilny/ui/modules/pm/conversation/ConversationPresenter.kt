package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class ConversationPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val pmApi: PMApi) : BasePresenter<ConversationView>() {
    lateinit var user : String
    fun loadConversation() {
        subscriptionHelperApi.subscribe(pmApi.getConversation(user),
                { view?.showConversation(it) },
                { view?.showErrorDialog(it) }, this)
    }

    fun sendMessage(body: String, photo : String?) {
        subscriptionHelperApi.subscribe(pmApi.sendMessage(body, user, photo),
                {
                    view?.hideInputbarProgress()
                    view?.resetInputbarState()
                    loadConversation()
                }, {
                    view?.hideInputbarProgress()
                    view?.showErrorDialog(it)
                }, this)
    }

    fun sendMessage(body: String, photo: TypedInputStream) {
        subscriptionHelperApi.subscribe(pmApi.sendMessage(body, user, photo),
                {
                    view?.hideInputbarProgress()
                    view?.resetInputbarState()
                    loadConversation()
                }, {
            view?.hideInputbarProgress()
            view?.showErrorDialog(it)
        }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelperApi.dispose(this)
    }
}