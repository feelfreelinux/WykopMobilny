package io.github.feelfreelinux.wykopmobilny.ui.input.entry.comment.add

import android.renderscript.ScriptGroup
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.input.BaseInputPresenter
import io.github.feelfreelinux.wykopmobilny.ui.input.InputPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class AddEntryCommentPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val entriesApi: EntriesApi) : InputPresenter<AddEntryCommentView>() {
    override fun sendWithPhoto(photo: TypedInputStream) {
        subscriptionHelper.subscribe(
                entriesApi.addEntryComment(view?.textBody!!, view?.entryId!!, photo),
                postSendCallback, { view?.showErrorDialog(it) }, this)
    }

    override fun sendWithPhotoUrl(photo: String?) {
        subscriptionHelper.subscribe(
                entriesApi.addEntryComment(view?.textBody!!, view?.entryId!!, photo),
                postSendCallback, { view?.showErrorDialog(it) }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}