package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EditEntryCommentPresenter(private val subscriptionHelperApi: SubscriptionHelperApi, private val entriesApi: EntriesApi) : InputPresenter<EditEntryCommentView>() {
    fun editComment() {
        view?.showProgressBar = true
        subscriptionHelperApi.subscribe(entriesApi.editEntryComment(view?.textBody!!, view?.entryId!!, view?.commentId!!),
                { view?.exitActivity() },
                {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                }, this)
    }

    override fun sendWithPhoto(photo: TypedInputStream) { editComment() }

    override fun sendWithPhotoUrl(photo: String?) { editComment() }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelperApi.dispose(this)
    }
}