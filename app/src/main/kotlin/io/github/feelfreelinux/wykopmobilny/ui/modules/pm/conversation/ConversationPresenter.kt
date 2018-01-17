package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class ConversationPresenter(val schedulers: Schedulers, val pmApi: PMApi) : BasePresenter<ConversationView>() {
    lateinit var user : String
    fun loadConversation() {
        compositeObservable.add(
                pmApi.getConversation(user)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.showConversation(it) },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    fun sendMessage(body: String, photo : String?, containsAdultContent: Boolean) {
        compositeObservable.add(
                pmApi.sendMessage(body, user, photo, containsAdultContent)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.hideInputbarProgress()
                                    view?.resetInputbarState()
                                    loadConversation()
                                },
                                {
                                    view?.hideInputbarProgress()
                                    view?.showErrorDialog(it)
                                })
        )
    }

    fun sendMessage(body: String, photo: TypedInputStream, containsAdultContent : Boolean) {
        compositeObservable.add(
                pmApi.sendMessage(body, user, containsAdultContent, photo)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    view?.hideInputbarProgress()
                                    view?.resetInputbarState()
                                    loadConversation()
                                },
                                {
                                    view?.hideInputbarProgress()
                                    view?.showErrorDialog(it)
                                })
        )
    }
}