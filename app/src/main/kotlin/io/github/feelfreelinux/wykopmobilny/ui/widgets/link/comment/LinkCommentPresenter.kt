package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

class LinkCommentPresenter(
        val schedulers: Schedulers,
        val newNavigatorApi: NewNavigatorApi,
        val wykopLinkHandlerApi: WykopLinkHandlerApi,
        val linksApi : LinksApi) : BasePresenter<LinkCommentView>() {
    var linkId = -1

    fun handleUrl(url : String) {
        wykopLinkHandlerApi.handleUrl(url)
    }

    fun voteUp() {
        compositeObservable.add(
                linksApi
                        .commentVoteUp(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.markVotedPlus()
                            view?.setVoteCount(it)
                        }, { view?.showErrorDialog(it) })
        )
    }

    fun voteDown() {
        compositeObservable.add(
                linksApi
                        .commentVoteDown(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.markVotedMinus()
                            view?.setVoteCount(it)
                        }, { view?.showErrorDialog(it) })
        )
    }


    fun voteCancel() {
        compositeObservable.add(
                linksApi
                        .commentVoteCancel(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.setVoteCount(it)
                            view?.markVoteRemoved()
                        }, { view?.showErrorDialog(it) })
        )
    }

    fun deleteComment() {
        compositeObservable.add(
                linksApi.commentDelete(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.markCommentAsRemoved()
                        }, { view?.showErrorDialog(it) })
        )
    }

    fun openEditCommentActivity(body : String) {
        newNavigatorApi.openEditLinkCommentActivity(body, linkId)
    }
}