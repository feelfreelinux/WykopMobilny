package io.github.feelfreelinux.wykopmobilny.ui.modules.links.linkdetails

import io.github.feelfreelinux.wykopmobilny.api.WykopImageFile
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.fragments.link.LinkHeaderActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.link.LinkInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentInteractor
import io.reactivex.Single

class LinkDetailsPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi,
    private val linkCommentInteractor: LinkCommentInteractor,
    private val linkHeaderInteractor: LinkInteractor
) : BasePresenter<LinkDetailsView>(), LinkCommentActionListener, LinkHeaderActionListener {

    var sortBy = "best"
    var linkId = -1

    override fun digLink(link: Link) =
        linkHeaderInteractor.digLink(link).processLinkSingle(link)

    override fun buryLink(link: Link, reason: Int) =
        linkHeaderInteractor.buryLink(link, reason).processLinkSingle(link)

    override fun removeVote(link: Link) =
        linkHeaderInteractor.removeVote(link).processLinkSingle(link)

    override fun markFavorite(link: Link) =
        linkHeaderInteractor.markFavorite(link).processLinkSingle(link)

    override fun digComment(comment: LinkComment) =
        linkCommentInteractor.commentVoteUp(comment).processLinkCommentSingle(comment)

    override fun buryComment(comment: LinkComment) =
        linkCommentInteractor.commentVoteDown(comment).processLinkCommentSingle(comment)

    override fun removeVote(comment: LinkComment) =
        linkCommentInteractor.commentVoteCancel(comment).processLinkCommentSingle(comment)

    override fun deleteComment(comment: LinkComment) =
        linkCommentInteractor.removeComment(comment).processLinkCommentSingle(comment)

    fun loadComments(scrollCommentId: Int? = null) {
        compositeObservable.add(
            linksApi.getLinkComments(linkId, sortBy)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    view?.showLinkComments(it)
                    scrollCommentId?.let {
                        view?.scrollToComment(scrollCommentId)
                    }
                }, { view?.showErrorDialog(it) })
        )
    }

    fun loadLinkAndComments(scrollCommentId: Int? = null) {
        compositeObservable.add(
            linksApi.getLink(linkId)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    view?.updateLink(it)
                    loadComments(scrollCommentId)
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

    fun sendReply(body: String, typedInputStream: WykopImageFile, containsAdultContent: Boolean) {
        val replyCommentId = view!!.getReplyCommentId()
        if (replyCommentId != -1) {
            compositeObservable.add(
                linksApi.commentAdd(body, containsAdultContent, typedInputStream, linkId, replyCommentId)
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
                linksApi.commentAdd(body, containsAdultContent, typedInputStream, linkId)
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

    fun sendReply(body: String, embed: String?, containsAdultContent: Boolean) {
        val replyCommentId = view!!.getReplyCommentId()
        if (replyCommentId != -1) {
            compositeObservable.add(
                linksApi.commentAdd(body, embed, containsAdultContent, linkId, replyCommentId)
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
                linksApi.commentAdd(body, embed, containsAdultContent, linkId)
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

    private fun Single<LinkComment>.processLinkCommentSingle(link: LinkComment) {
        compositeObservable.add(
            this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.updateLinkComment(it) },
                    {
                        view?.showErrorDialog(it)
                        view?.updateLinkComment(link)
                    })
        )
    }

    private fun Single<Link>.processLinkSingle(link: Link) {
        compositeObservable.add(
            this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.updateLink(it) },
                    {
                        view?.showErrorDialog(it)
                        view?.updateLink(link)
                    })
        )
    }
}