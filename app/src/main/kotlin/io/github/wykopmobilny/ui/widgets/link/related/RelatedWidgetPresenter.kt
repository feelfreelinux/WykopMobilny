package io.github.wykopmobilny.ui.widgets.link.related

import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.utils.intoComposite
import io.github.wykopmobilny.utils.linkhandler.WykopLinkHandlerApi

class RelatedWidgetPresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi,
    val linkHandlerApi: WykopLinkHandlerApi
) : BasePresenter<RelatedWidgetView>() {

    var relatedId = -1

    fun handleLink(url: String) = linkHandlerApi.handleUrl(url)

    fun voteUp() {
        linksApi.relatedVoteUp(relatedId)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.setVoteCount(it.voteCount)
                    view?.markVoted()
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    fun voteDown() {
        linksApi.relatedVoteDown(relatedId)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.setVoteCount(it.voteCount)
                    view?.markUnvoted()
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }
}
