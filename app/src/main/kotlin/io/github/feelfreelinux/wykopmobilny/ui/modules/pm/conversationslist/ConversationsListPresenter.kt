package io.github.feelfreelinux.wykopmobilny.ui.modules.pm.conversationslist

import io.github.feelfreelinux.wykopmobilny.api.pm.PMApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

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
