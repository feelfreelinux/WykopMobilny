package io.github.feelfreelinux.wykopmobilny.ui.modules.links.upcoming

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class UpcomingPresenter(
    val schedulers: Schedulers,
    val linksInteractor: LinksInteractor,
    private val linksApi: LinksApi
) : BasePresenter<UpcomingView>(), LinkActionListener {

    companion object {
        const val SORTBY_COMMENTS = "comments"
        const val SORTBY_VOTES = "votes"
        const val SORTBY_DATE = "date"
        const val SORTBY_ACTIVE = "active"
    }

    var page = 1
    var sortBy = "comments"

    fun getUpcomingLinks(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        linksApi.getUpcoming(page, sortBy)
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

    fun Single<Link>.processLinkSingle(link: Link) {
        this.subscribeOn(schedulers.backgroundThread())
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
