package io.github.wykopmobilny.ui.modules.favorite.links

import io.github.wykopmobilny.api.links.LinksApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor
import io.github.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class LinksFavoritePresenter(
    val schedulers: Schedulers,
    val linksApi: LinksApi,
    val linksInteractor: LinksInteractor
) : BasePresenter<LinksFavoriteView>(), LinkActionListener {

    var page = 1

    fun loadData(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        linksApi.getObserved(page)
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

    override fun dig(link: Link) = linksInteractor.dig(link).processLinkSingle(link)

    override fun removeVote(link: Link) = linksInteractor.voteRemove(link).processLinkSingle(link)

    private fun Single<Link>.processLinkSingle(link: Link) {
        this
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.updateLink(it) },
                {
                    view?.showErrorDialog(it)
                    view?.updateLink(link)
                }
            )
            .intoComposite(compositeObservable)
    }
}
