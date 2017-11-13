package io.github.feelfreelinux.wykopmobilny.ui.dialogs.votersdialog

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class VotersDialogPresenter(val subscriptionHelperApi: SubscriptionHelperApi, val entriesApi: EntriesApi) : BasePresenter<VotersDialogView>() {
    var entryId = -1
    var commentId = -1

    fun getVoters() {
        subscriptionHelperApi.subscribe(entriesApi.getEntryVoters(entryId),
                { view?.showVoters(it) }, { view?.showErrorDialog(it) }, this)
    }

    fun getCommentVoters() {
        subscriptionHelperApi.subscribe(entriesApi.getEntryCommentVoters(commentId),
                { view?.showVoters(it) }, { view?.showErrorDialog(it) }, this)
    }
}