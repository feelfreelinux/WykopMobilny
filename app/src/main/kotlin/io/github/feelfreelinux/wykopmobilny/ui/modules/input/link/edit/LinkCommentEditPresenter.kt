package io.github.feelfreelinux.wykopmobilny.ui.modules.input.link.edit

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.BaseInputView
import io.github.feelfreelinux.wykopmobilny.ui.modules.input.InputPresenter

class LinkCommentEditPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi
) : InputPresenter<BaseInputView>() {

    var linkCommentId: Int = -1

    private fun editLinkComment() {
        view?.showProgressBar = true
        compositeObservable.add(
            linksApi.commentEdit(view?.textBody!!, linkCommentId)
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

    override fun sendWithPhoto(photo: WykopImageFile, containsAdultContent: Boolean) = editLinkComment()

    override fun sendWithPhotoUrl(photo: String?, containsAdultContent: Boolean) = editLinkComment()
}