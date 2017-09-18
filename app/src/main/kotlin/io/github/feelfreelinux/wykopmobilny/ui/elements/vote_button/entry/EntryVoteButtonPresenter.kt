package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.base.BaseVoteButtonPresenter
import io.github.feelfreelinux.wykopmobilny.api.enqueue

class EntryVoteButtonPresenter(private val entriesApi : EntriesApi) : BasePresenter<EntryVoteButtonView>(), BaseVoteButtonPresenter  {
    var entryId = 0

    override fun unvote() {
        entriesApi.unvoteEntry(entryId).enqueue(
                {
                    val voteCount = it.body()!!.vote
                    view?.voteCount = voteCount
                    view?.isButtonSelected = false
                },
                { view?.showErrorDialog(it) }
        )
    }

    override fun vote() {
        entriesApi.voteEntry(entryId).enqueue(
                {
                    val voteCount = it.body()!!.vote
                    view?.voteCount = voteCount
                    view?.isButtonSelected = true
                },
                { view?.showErrorDialog(it) }
        )
    }
}