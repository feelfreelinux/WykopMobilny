package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputView
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit.EditEntryView
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class EditEntryPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val entriesApi: EntriesApi) : InputPresenter<EditEntryView>() {
    fun editEntry() {
        view?.showProgressBar = true
        subscriptionHelper.subscribe(entriesApi.editEntry(view?.textBody!!, view?.entryId!!),
                { view?.exitActivity() }, {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                }, this)
    }

    override fun sendWithPhoto(photo: TypedInputStream) { editEntry() }

    override fun sendWithPhotoUrl(photo: String?) { editEntry() }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}