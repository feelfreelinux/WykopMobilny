package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputView
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class AddEntryPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val entriesApi: EntriesApi) : InputPresenter<BaseInputView>() {
    override fun sendWithPhoto(photo: TypedInputStream) {
        view?.showProgressBar = true
        subscriptionHelper.subscribe(entriesApi.addEntry(view?.textBody!!, photo),
                { view?.exitActivity() },
                {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                }, this)
    }

    override fun sendWithPhotoUrl(photo: String?) {
        view?.showProgressBar = true
        subscriptionHelper.subscribe(entriesApi.addEntry(view?.textBody!!, photo),
                { view?.exitActivity() },
                {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                }, this)
    }

    override fun unsubscribe() {
        super.unsubscribe()
        subscriptionHelper.dispose(this)
    }
}