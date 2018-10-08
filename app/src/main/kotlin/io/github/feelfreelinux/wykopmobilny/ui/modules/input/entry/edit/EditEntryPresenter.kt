package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class EditEntryPresenter(
    private val schedulers: Schedulers,
    private val entriesApi: EntriesApi
) : InputPresenter<EditEntryView>() {

    private fun editEntry() {
        view?.showProgressBar = true
        entriesApi.editEntry(view?.textBody!!, view?.entryId!!)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.exitActivity() },
                {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                })
            .intoComposite(compositeObservable)
    }

    override fun sendWithPhoto(photo: WykopImageFile, containsAdultContent: Boolean) = editEntry()

    override fun sendWithPhotoUrl(photo: String?, containsAdultContent: Boolean) = editEntry()
}