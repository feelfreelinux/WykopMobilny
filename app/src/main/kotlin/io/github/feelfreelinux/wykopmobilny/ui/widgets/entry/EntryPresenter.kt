package io.github.feelfreelinux.wykopmobilny.ui.widgets.entry

import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.ClipboardHelperApi
import io.github.feelfreelinux.wykopmobilny.utils.textview.removeHtml
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

@AutoFactory()
class EntryPresenter(@Provided val schedulers: Schedulers,
                     @Provided val entriesApi: EntriesApi,
                     @Provided val clipboardHelperApi: ClipboardHelperApi,
                     @Provided val navigatorApi: NewNavigatorApi,
                     @Provided val linkHandler: WykopLinkHandlerApi) : BasePresenter<EntryView>() {
    var entryId = -1
    fun deleteEntry() {
        compositeObservable.add(
                entriesApi.deleteEntry(entryId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markEntryAsRemoved() }, { view?.showErrorDialog(it) })
        )
    }

    fun voteEntry() {
        compositeObservable.add(
                entriesApi.voteEntry(entryId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markEntryVoted(it.voteCount) }, { view?.showErrorDialog(it) })
        )
    }

    fun unvoteEntry() {
        compositeObservable.add(
                entriesApi.unvoteEntry(entryId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markEntryUnvoted(it.voteCount) }, { view?.showErrorDialog(it) })
        )
    }

    fun markFavorite() {
        compositeObservable.add(
                entriesApi.markFavorite(entryId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markEntryFavorite(it.userFavorite) }, { view?.showErrorDialog(it) })
        )
    }

    fun voteAnswer(answerId : Int) {
        compositeObservable.add(
                entriesApi.voteSurvey(entryId, answerId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.showSurvey(it) },
                                { view?.showErrorDialog(it) })
        )
    }

    fun getVoters() {
        compositeObservable.add(
                entriesApi.getEntryVoters(entryId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe(
                                { view?.showVoters(it) },
                                { view?.showErrorDialog(it) }
                        )
        )
    }

    fun copyContent(entry : Entry) {
        clipboardHelperApi.copyTextToClipboard(entry.body.removeHtml(), "entry-text-copied")
    }

    fun editEntry(entry : Entry) {
        navigatorApi.openEditEntryActivity(entry.body, entry.id)
    }

    fun handleLink(url : String) {
        linkHandler.handleUrl(url, false)
    }

    fun openDetails() {
        navigatorApi.openEntryDetailsActivity(entryId)
    }

    fun reportContent() {
        navigatorApi.openReportEntryScreen(entryId)
    }
}