package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.comments

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.LinkComment
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.linkcomments.LinkCommentInteractor
import io.reactivex.Single

class ProfileLinksFragmentPresenter(val schedulers : Schedulers, val profileApi : ProfileApi, val linksInteractor: LinkCommentInteractor) : BasePresenter<ProfileLinkCommentsView>(), LinkCommentActionListener {
    override fun removeVote(comment: LinkComment) {
        linksInteractor.commentVoteCancel(comment).processLinkCommentSingle(comment)
    }

    var page = 1
    lateinit var username : String
    fun loadData(shouldRefresh : Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
                profileApi.getLinkComments(username, page)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                {
                                    if (it.isNotEmpty()) {
                                        page++
                                        view?.addItems(it, shouldRefresh)
                                    } else view?.disableLoading()
                                },
                                { view?.showErrorDialog(it) }
                        )
        )
    }
    override fun digComment(comment: LinkComment) {
        linksInteractor.commentVoteUp(comment).processLinkCommentSingle(comment)
    }

    override fun buryComment(comment: LinkComment) {
        linksInteractor.commentVoteDown(comment).processLinkCommentSingle(comment)
    }

    override fun deleteComment(comment: LinkComment) {
        linksInteractor.removeComment(comment).processLinkCommentSingle(comment)
    }

    fun Single<LinkComment>.processLinkCommentSingle(link : LinkComment) {
        compositeObservable.add(
                this
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.updateComment(it) },
                                {
                                    view?.showErrorDialog(it)
                                    view?.updateComment(link)
                                })
        )
    }
}