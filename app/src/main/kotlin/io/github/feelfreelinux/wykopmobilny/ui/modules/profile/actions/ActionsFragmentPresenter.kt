package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.actions

import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntriesInteractor
import io.github.feelfreelinux.wykopmobilny.ui.fragments.entries.EntryActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.reactivex.Single

class ActionsFragmentPresenter(
    val schedulers: Schedulers,
    val profileApi: ProfileApi,
    val entriesInteractor: EntriesInteractor,
    val linksInteractor: LinksInteractor,
    val entriesApi: EntriesApi
) : BasePresenter<ActionsView>(), LinkActionListener, EntryActionListener {

    lateinit var username: String

    fun getActions() {
        compositeObservable.add(
            profileApi.getActions(username)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    view?.addItems(it, true)
                    view?.disableLoading()
                }, { view?.showErrorDialog(it) })
        )
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
        compositeObservable.add(
            entriesApi.getEntryVoters(entry.id)
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({
                    view?.showVoters(it)
                }, {
                    view?.showErrorDialog(it)
                })
        )
    }

    override fun dig(link: Link) =
        linksInteractor.dig(link).processLinkSingle(link)

    override fun removeVote(link: Link) =
        linksInteractor.voteRemove(link).processLinkSingle(link)

    private fun Single<Entry>.processEntrySingle(entry: Entry) {
        compositeObservable.add(
            this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.updateEntry(it) },
                    {
                        view?.showErrorDialog(it)
                        view?.updateEntry(entry)
                    })
        )
    }

    private fun Single<Link>.processLinkSingle(link: Link) {
        compositeObservable.add(
            this
                .subscribeOn(schedulers.backgroundThread())
                .observeOn(schedulers.mainThread())
                .subscribe({ view?.updateLink(it) },
                    {
                        view?.showErrorDialog(it)
                        view?.updateLink(link)
                    })
        )
    }
}