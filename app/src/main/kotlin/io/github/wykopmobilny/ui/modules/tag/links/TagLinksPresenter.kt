package io.github.wykopmobilny.ui.modules.tag.links

import io.github.wykopmobilny.api.tag.TagApi
import io.github.wykopmobilny.base.BasePresenter
import io.github.wykopmobilny.base.Schedulers
import io.github.wykopmobilny.models.dataclass.Link
import io.github.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.wykopmobilny.ui.fragments.links.LinksInteractor
import io.github.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class TagLinksPresenter(
    val schedulers: Schedulers,
    val tagApi: TagApi,
    val linksInteractor: LinksInteractor
) : BasePresenter<TagLinksView>(), LinkActionListener {

    var page = 1
    var tag = ""

    fun loadData(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        tagApi.getTagLinks(tag, page)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                {
                    view?.setParentMeta(it.meta)
                    if (it.entries.isNotEmpty()) {
                        page++
                        view?.addItems(it.entries, shouldRefresh)
                    } else view?.disableLoading()
                },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    override fun dig(link: Link) {
        linksInteractor.dig(link).processLinkSingle(link)
    }

    override fun removeVote(link: Link) {
        linksInteractor.voteRemove(link).processLinkSingle(link)
    }

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
