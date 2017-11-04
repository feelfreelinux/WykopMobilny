package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.base.BaseVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryVoteButtonPresenter(private val subscriptionHandler : SubscriptionHelperApi, private val entriesApi : EntriesApi) : BasePresenter<EntryVoteButtonView>(), BaseVoteButtonPresenter  {
    var entryId = 0

    override fun unvote() {
                subscriptionHandler.subscribe(entriesApi.unvoteEntry(entryId),
                        {
                            view?.voteCount = it.voteCount
                            view?.isButtonSelected = false
                        }, { view?.showErrorDialog(it) }, this)
    }

    override fun vote() {
                subscriptionHandler.subscribe(entriesApi.voteEntry(entryId),
                        {
                            view?.voteCount = it.voteCount
                            view?.isButtonSelected = true
                        }, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHandler.dispose(this)
    }
}