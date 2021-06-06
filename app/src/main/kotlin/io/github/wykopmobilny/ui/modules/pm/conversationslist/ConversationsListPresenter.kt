package io.github.wykopmobilny.ui.modules.pm.conversationslist

import io.github.wykopmobilny.api.pm.PMApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.intoComposite

class ConversationsListPresenter(
    private val schedulers: Schedulers,
    private val pmApi: PMApi
) : BasePresenter<ConversationsListView>() {

    fun loadConversations() {
        pmApi.getConversations()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.showConversations(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}
