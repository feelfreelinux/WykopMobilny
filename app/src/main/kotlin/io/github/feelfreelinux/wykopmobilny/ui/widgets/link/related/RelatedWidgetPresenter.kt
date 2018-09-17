package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.related

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

class RelatedWidgetPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : BasePresenter<RelatedWidgetView>() {

    var relatedId = -1

    fun handleLink(url: String) = linkHandlerApi.handleUrl(url)

    fun voteUp() {
        compositeObservable.add(
            linksApi.relatedVoteUp(relatedId)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    view?.setVoteCount(it.voteCount)
                    view?.markVoted()
                }, { view?.showErrorDialog(it) })
        )
    }

    fun voteDown() {
        compositeObservable.add(
            linksApi.relatedVoteDown(relatedId)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    view?.setVoteCount(it.voteCount)
                    view?.markUnvoted()
                }, { view?.showErrorDialog(it) })
        )
    }
}