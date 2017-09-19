package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base.BaseVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EntryCommentVoteButtonPresenter(private val subscriptionHandler : SubscriptionHelperApi, private val entriesApi : EntriesApi) : BasePresenter<EntryCommentVoteButtonView>(), BaseVoteButtonPresenter  {
    var entryId = 0
    var commentId = 0

    override fun unvote() {
        subscriptions.add(
            subscriptionHandler.subscribeOnSchedulers(entriesApi.unvoteComment(entryId, commentId))
                .subscribe({
                    view?.voteCount = it.vote
                    view?.isButtonSelected = false
                }, { view?.showErrorDialog(it) })
        )
    }

    override fun vote() {
        subscriptions.add(
            subscriptionHandler.subscribeOnSchedulers(entriesApi.voteComment(entryId, commentId))
                .subscribe({
                    view?.voteCount = it.vote
                    view?.isButtonSelected = true
                }, { view?.showErrorDialog(it) })
        )
    }
}