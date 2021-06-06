package io.github.wykopmobilny.ui.modules.input.link.edit

import io.github.wykopmobilny.api.WykopImageFile
import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.ui.modules.input.BaseInputView
import io.github.wykopmobilny.ui.modules.input.InputPresenter
import io.github.wykopmobilny.utils.intoComposite

class LinkCommentEditPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi
) : InputPresenter<BaseInputView>() {

    var linkCommentId: Int = -1

    private fun editLinkComment() {
        view?.showProgressBar = true
        linksApi.commentEdit(view?.textBody!!, linkCommentId)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.exitActivity() },
                {
                    view?.showProgressBar = false
                    view?.showErrorDialog(it)
                }
            )
            .intoComposite(compositeObservable)
    }

    override fun sendWithPhoto(photo: WykopImageFile, containsAdultContent: Boolean) = editLinkComment()

    override fun sendWithPhotoUrl(photo: String?, containsAdultContent: Boolean) = editLinkComment()
}
