package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversation

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class ConversationPresenter(
    val schedulers: Schedulers,
    private val pmApi: PMApi
) : BasePresenter<ConversationView>() {

    lateinit var user: String

    fun loadConversation() {
        pmApi.getConversation(user)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.showConversation(it) },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    fun sendMessage(body: String, photo: String?, containsAdultContent: Boolean) {
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
            .intoComposite(compositeObservable)
    }

    fun sendMessage(body: String, photo: WykopImageFile, containsAdultContent: Boolean) {
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
            .intoComposite(compositeObservable)
    }
}