package io.github.feelfreelinux.wykopmobilny.ui.modules.input.entry.comment

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter

class EditEntryCommentPresenter(private val schedulers: Schedulers, private val entriesApi: EntriesApi) : InputPresenter<EditEntryCommentView>() {
    fun editComment() {
        view?.showProgressBar = true
        compositeObservable.add(
                entriesApi.editEntryComment(view?.textBody!!, view?.commentId!!)
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

    override fun sendWithPhoto(photo: TypedInputStream) { editComment() }

    override fun sendWithPhotoUrl(photo: String?) { editComment() }
}