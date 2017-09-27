package io.github.feelfreelinux.wykopmobilny.ui.input.entry.add

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.BaseView
import io.github.feelfreelinux.wykopmobilny.ui.input.BaseInputPresenter
import io.github.feelfreelinux.wykopmobilny.ui.input.BaseInputView
import io.github.feelfreelinux.wykopmobilny.ui.input.InputPresenter
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelperApi

class AddEntryPresenter(private val subscriptionHelper: SubscriptionHelperApi, private val entriesApi: EntriesApi) : InputPresenter<BaseInputView>() {
    override fun sendWithPhoto(photo: TypedInputStream) {
            subscriptionHelper.subscribeOnSchedulers(entriesApi.addEntry(view?.textBody!!, photo))
                    .subscribe(postSendCallback, { view?.showErrorDialog(it) })
    }

    override fun sendWithPhotoUrl(photo: String) {
        subscriptionHelper.subscribeOnSchedulers(entriesApi.addEntry(view?.textBody!!, photo))
                .subscribe(postSendCallback, { view?.showErrorDialog(it) })
    }
}