package io.github.wykopmobilny.ui.modules.profile.links.comments

import io.github.wykopmobilny.api.profile.ProfileApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.models.dataclass.LinkComment
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentActionListener
import io.github.wykopmobilny.ui.fragments.linkcomments.LinkCommentInteractor
import io.github.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class ProfileLinksFragmentPresenter(
    val schedulers: Schedulers,
    val profileApi: ProfileApi,
    val linksInteractor: LinkCommentInteractor
) : BasePresenter<ProfileLinkCommentsView>(), LinkCommentActionListener {

    var page = 1
    lateinit var username: String

    fun loadData(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
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
            .intoComposite(compositeObservable)
    }

    override fun removeVote(comment: LinkComment) =
        linksInteractor.commentVoteCancel(comment).processLinkCommentSingle(comment)

    override fun digComment(comment: LinkComment) =
        linksInteractor.commentVoteUp(comment).processLinkCommentSingle(comment)

    override fun buryComment(comment: LinkComment) =
        linksInteractor.commentVoteDown(comment).processLinkCommentSingle(comment)

    override fun deleteComment(comment: LinkComment) =
        linksInteractor.removeComment(comment).processLinkCommentSingle(comment)

    private fun Single<LinkComment>.processLinkCommentSingle(link: LinkComment) {
        this
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.updateComment(it) },
                {
                    view?.showErrorDialog(it)
                    view?.updateComment(link)
                }
            )
            .intoComposite(compositeObservable)
    }
}
