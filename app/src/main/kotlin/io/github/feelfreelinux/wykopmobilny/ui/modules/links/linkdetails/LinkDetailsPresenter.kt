package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import io.github.feelfreelinux.wykopmobilny.api.entries.TypedInputStream
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Embed
import io.github.feelfreelinux.wykopmobilny.utils.printout

class LinkDetailsPresenter(val schedulers: Schedulers, val linksApi: LinksApi) : BasePresenter<LinkDetailsView>() {
    var sortBy = "best"
    var linkId = -1
    fun loadComments(scrollCommentId : Int? = null) {
        compositeObservable.add(
                linksApi.getLinkComments(linkId, sortBy)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showLinkComments(it)
                            scrollCommentId?.let {
                                view?.scrollToComment(scrollCommentId) }
                        }, { view?.showErrorDialog(it) })
        )
    }

    fun updateLink() {
        compositeObservable.add(
                linksApi.getLink(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.updateLink(it) }, { view?.showErrorDialog(it) })
        )
    }

    fun sendReply(body : String, typedInputStream: TypedInputStream) {
        val replyCommentId = view!!.getReplyCommentId()
        if (replyCommentId != -1) {
            compositeObservable.add(
                    linksApi.commentAdd(body, typedInputStream, linkId, replyCommentId)
                            .subscribeOn(schedulers.backgroundThread())
                            .observeOn(schedulers.mainThread())
                            .subscribe({
                                view?.resetInputbarState()
                                view?.hideInputToolbar()
                                loadComments(it.id)
                            }, {
                                view?.showErrorDialog(it)
                                view?.hideInputbarProgress()
                            })
            )
        } else {
            compositeObservable.add(
                    linksApi.commentAdd(body, typedInputStream, linkId)
                            .subscribeOn(schedulers.backgroundThread())
                            .observeOn(schedulers.mainThread())
                            .subscribe({
                                view?.resetInputbarState()
                                view?.hideInputToolbar()
                                loadComments(it.id)
                            }, {
                                view?.showErrorDialog(it)
                                view?.hideInputbarProgress()
                            })
            )
        }
    }

    fun sendReply(body : String, embed: String?) {
        val replyCommentId = view!!.getReplyCommentId()
        if (replyCommentId != -1) {
            compositeObservable.add(
                    linksApi.commentAdd(body, embed, linkId, replyCommentId)
                            .subscribeOn(schedulers.backgroundThread())
                            .observeOn(schedulers.mainThread())
                            .subscribe({
                                view?.resetInputbarState()
                                view?.hideInputToolbar()
                                loadComments(it.id)
                            }, {
                                view?.showErrorDialog(it)
                                view?.hideInputbarProgress()
                            })
            )
        } else {
            compositeObservable.add(
                    linksApi.commentAdd(body, embed, linkId)
                            .subscribeOn(schedulers.backgroundThread())
                            .observeOn(schedulers.mainThread())
                            .subscribe({
                                view?.resetInputbarState()
                                view?.hideInputToolbar()
                                loadComments(it.id)
                            }, {
                                view?.showErrorDialog(it)
                                view?.hideInputbarProgress()
                            })
            )
        }
    }
}