package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.add

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class AddEntryPresenter(
    private val schedulers: Schedulers,
    private val entriesApi: EntriesApi
) : InputPresenter<AddEntryActivityView>() {

    override fun sendWithPhoto(photo: WykopImageFile, containsAdultContent: Boolean) {
        view?.showProgressBar = true
        entriesApi.addEntry(view?.textBody!!, photo, containsAdultContent)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.openEntryActivity(it.id) },
                {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                }
            )
            .intoComposite(compositeObservable)
    }

    override fun sendWithPhotoUrl(photo: String?, containsAdultContent: Boolean) {
        view?.showProgressBar = true
        entriesApi.addEntry(view?.textBody!!, photo, containsAdultContent)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.openEntryActivity(it.id) },
                {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                }
            )
            .intoComposite(compositeObservable)
    }
}
