package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi
import io.github.feelfreelinux.wykopmobilny.utils.wykop_link_handler.WykopLinkHandlerApi

class LinkPresenter(
        val schedulers: Schedulers,
        val navigatorApi: NewNavigatorApi,
        val linkHandlerApi: WykopLinkHandlerApi,
        val linksApi: LinksApi) : BasePresenter<LinkView>() {
    override fun subscribe(view: LinkView) {
        super.subscribe(view)
        compositeObservable.addAll(
                linksApi.digSubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe {
                            if (linkId == it.linkId) {
                                view?.showDigged()
                                view?.showVoteCount(it.voteResponse)
                            }
                        },
                linksApi.burySubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe {
                            if (linkId == it.linkId) {
                                view?.showBurried()
                                view?.showVoteCount(it.voteResponse)
                            }
                        },
                linksApi.voteRemoveSubject
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe {
                            if (linkId == it.linkId) {
                                view?.showUnvoted()
                                view?.showVoteCount(it.voteResponse)
                            }
                        }
        )
    }

    var linkId = -1

    companion object {
        val BURY_REASON_DUPLICATE = 1
        val BURY_REASON_SPAM = 2
        val BURY_REASON_FAKE_INFO = 3
        val BURY_REASON_WRONG_CONTENT = 4
        val BURY_REASON_UNSUITABLE_CONTENT = 5
    }

    fun handleUrl(url : String) {
        linkHandlerApi.handleUrl(url)
    }

    fun openProfile(nickname : String) {
        navigatorApi.openProfileActivity(nickname)
    }

    fun openUpvotersList() {
        navigatorApi.openLinkUpvotersActivity(linkId)
    }

    fun openDownvotersList() {
        navigatorApi.openLinkDownvotersActivity(linkId)
    }

    fun openRelatedList() {
        navigatorApi.openLinkRelatedActivity(linkId)
    }

    fun voteUp() {
        compositeObservable.add(
                linksApi.voteUp(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showDigged()
                            view?.showVoteCount(it)
                        }, {
                            view?.showErrorDialog(it)
                        })
        )
    }

    fun voteRemove() {
        compositeObservable.add(
                linksApi.voteRemove(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showUnvoted()
                            view?.showVoteCount(it)
                        }, {
                            view?.showErrorDialog(it)
                        })
        )
    }

    fun voteDown(reason : Int) {
        compositeObservable.add(
                linksApi.voteDown(linkId, reason)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showBurried()
                        }, {
                            view?.showErrorDialog(it)
                        })
        )
    }

    fun markFavorite() {
        compositeObservable.add(
                linksApi.markFavorite(linkId)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.markFavorite() }, { view?.showErrorDialog(it) })
        )
    }
}