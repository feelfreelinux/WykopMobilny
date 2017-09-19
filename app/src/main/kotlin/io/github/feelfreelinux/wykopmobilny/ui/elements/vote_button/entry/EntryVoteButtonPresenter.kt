package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base.BaseVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.api.enqueue
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryVoteButtonPresenter(val subscriptionHandler : SubscriptionHelperApi, private val entriesApi : EntriesApi) : BasePresenter<EntryVoteButtonView>(), BaseVoteButtonPresenter  {
    var entryId = 0

    override fun unvote() {
        subscriptions.add(
                subscriptionHandler.subscribeOnSchedulers(entriesApi.unvoteEntry(entryId))
                        .subscribe({
                            view?.voteCount = it.vote
                            view?.isButtonSelected = false
                        }, { view?.showErrorDialog(it) })
        )
    }

    override fun vote() {
        subscriptions.add(
                subscriptionHandler.subscribeOnSchedulers(entriesApi.voteEntry(entryId))
                        .subscribe({
                            view?.voteCount = it.vote
                            view?.isButtonSelected = true
                        }, { view?.showErrorDialog(it) })
        )
    }
}