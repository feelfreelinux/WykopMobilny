package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.edit

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter

class EditEntryPresenter(private val schedulers: Schedulers, private val entriesApi: EntriesApi) : InputPresenter<EditEntryView>() {
    fun editEntry() {
        view?.showProgressBar = true
        compositeObservable.add(
                entriesApi.editEntry(view?.textBody!!, view?.entryId!!)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.exitActivity() },
                                {
                                    view?.showProgressBar = false
                                    view?.showErrorDialog(it)
                                })
        )
    }

    override fun sendWithPhoto(photo: WykopImageFile, containsAdultContent : Boolean) { editEntry() }

    override fun sendWithPhotoUrl(photo: String?, containsAdultContent: Boolean) { editEntry() }
}