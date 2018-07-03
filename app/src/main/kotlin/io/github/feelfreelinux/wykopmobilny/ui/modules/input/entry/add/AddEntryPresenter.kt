package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputView
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter

class AddEntryPresenter(private val schedulers: Schedulers, private val entriesApi: EntriesApi) : InputPresenter<AddEntryActivityView>() {
    override fun sendWithPhoto(photo: TypedInputStream, containsAdultContent : Boolean) {
        view?.showProgressBar = true
        compositeObservable.add(
                entriesApi.addEntry(view?.textBody!!, photo, containsAdultContent)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.openEntryActivity(it.id) },
                                {
                                    view?.showProgressBar = false
                                    view?.showErrorDialog(it)
                                })
        )
    }

    override fun sendWithPhotoUrl(photo: String?, containsAdultContent : Boolean) {
        view?.showProgressBar = true
        compositeObservable.add(
                entriesApi.addEntry(view?.textBody!!, photo, containsAdultContent)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.openEntryActivity(it.id) },
                                {
                                    view?.showProgressBar = false
                                    view?.showErrorDialog(it)
                                })
        )
    }
}