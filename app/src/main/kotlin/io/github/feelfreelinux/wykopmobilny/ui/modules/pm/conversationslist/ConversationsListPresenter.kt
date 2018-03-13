package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class ConversationsListPresenter(private val schedulers: Schedulers, val pmApi: PMApi) : BasePresenter<ConversationsListView>() {
    fun loadConversations() {
        compositeObservable.add(
                pmApi.getConversations()
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showConversations(it) }, { view?.showErrorDialog(it) })
        )
    }
}