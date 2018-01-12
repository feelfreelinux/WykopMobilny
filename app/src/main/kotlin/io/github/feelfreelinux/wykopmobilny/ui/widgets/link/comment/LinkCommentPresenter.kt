package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.comment

import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi

@AutoFactory
class LinkCommentPresenter(
        @Provided val schedulers: Schedulers,
        @Provided val newNavigatorApi: NewNavigatorApi,
        @Provided val linksApi : LinksApi) : BasePresenter<LinkCommentView>() {
    var linkId = -1
    fun handleUrl(url : String) {
        newNavigatorApi.openBrowser(url)
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
}