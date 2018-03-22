package io.github.feelfreelinux.wykopmobilny.ui.widgets.link.linkitem

import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.usermanager.UserManagerApi

class LinkItemPresenter(val schedulers: Schedulers, val linksApi: LinksApi,
                        val userManagerApi: UserManagerApi) : BasePresenter<LinkItemView>() {
    override fun subscribe(view: LinkItemView) {
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

    fun voteUp() {
        compositeObservable.add(
                linksApi.voteUp(linkId, false)
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
                linksApi.voteRemove(linkId, false)
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
                linksApi.voteDown(linkId, reason, false)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({
                            view?.showBurried()
                        }, {
                            view?.showErrorDialog(it)
                        })
        )
    }
}