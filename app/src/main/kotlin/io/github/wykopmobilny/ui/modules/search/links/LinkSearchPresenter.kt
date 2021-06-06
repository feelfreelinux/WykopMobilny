package io.github.wykopmobilny.ui.modules.search.links

import io.github.wykopmobilny.api.search.SearchApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor
import io.github.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class LinkSearchPresenter(
    val schedulers: Schedulers,
    val searchApi: SearchApi,
    val linksInteractor: LinksInteractor
) : BasePresenter<LinkSearchView>(), LinkActionListener {

    var page = 1

    fun searchLinks(q: String, shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        searchApi.searchLinks(page, q)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.showSearchEmptyView = (page == 1 && it.isEmpty())
                    if (it.isNotEmpty()) {
                        page++
                        view?.addItems(it, shouldRefresh)
                    } else {
                        view?.addItems(it, (page == 1))
                        view?.disableLoading()
                    }
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
