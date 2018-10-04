package io.github.feelfreelinux.wykopmobilny.ui.modules.mywykop.index

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.api.mywykop.MyWykopApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entrylink.EntryLinkFragmentView
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite
import io.reactivex.Single

class MyWykopEntryLinkPresenter(
    val schedulers: Schedulers,
    val entriesApi: EntriesApi,
    val entriesInteractor: EntriesInteractor,
    val linksInteractor: LinksInteractor,
    val linksApi: LinksApi,
    private val myWykopApi: MyWykopApi
) : BasePresenter<EntryLinkFragmentView>(), EntryActionListener, LinkActionListener {

    var page = 1

    fun loadIndex(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        myWykopApi.getIndex(page)
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

    fun loadTags(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        myWykopApi.byTags(page)
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

    fun loadUsers(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        myWykopApi.byUsers(page)
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

    override fun voteEntry(entry: Entry) =
        entriesInteractor.voteEntry(entry).processEntrySingle(entry)

    override fun unvoteEntry(entry: Entry) =
        entriesInteractor.unvoteEntry(entry).processEntrySingle(entry)

    override fun markFavorite(entry: Entry) =
        entriesInteractor.markFavorite(entry).processEntrySingle(entry)

    override fun deleteEntry(entry: Entry) =
        entriesInteractor.deleteEntry(entry).processEntrySingle(entry)

    override fun voteSurvey(entry: Entry, index: Int) =
        entriesInteractor.voteSurvey(entry, index).processEntrySingle(entry)

    override fun getVoters(entry: Entry) {
        view?.openVotersMenu()
        entriesApi.getEntryVoters(entry.id)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({
                view?.showVoters(it)
            }, {
                view?.showErrorDialog(it)
            })
            .intoComposite(compositeObservable)
    }

    override fun dig(link: Link) =
        linksInteractor.dig(link).processLinkSingle(link)

    override fun removeVote(link: Link) =
        linksInteractor.voteRemove(link).processLinkSingle(link)

    private fun Single<Entry>.processEntrySingle(entry: Entry) {
        this
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.updateEntry(it) },
                {
                    view?.showErrorDialog(it)
                    view?.updateEntry(entry)
                })
            .intoComposite(compositeObservable)
    }

    private fun Single<Link>.processLinkSingle(link: Link) {
        this
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.updateLink(it) },
                {
                    view?.showErrorDialog(it)
                    view?.updateLink(link)
                })
            .intoComposite(compositeObservable)
    }
}