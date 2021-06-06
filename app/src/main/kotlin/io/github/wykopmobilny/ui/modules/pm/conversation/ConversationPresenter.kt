package io.github.wykopmobilny.ui.modules.pm.conversation

import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.pm.PMApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.intoComposite

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
                }
            )
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
                }
            )
            .intoComposite(compositeObservable)
    }
}
