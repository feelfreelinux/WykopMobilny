package io.github.feelfreelinux.wykopmobilny.ui.widgets.link

import com.google.auto.factory.AutoFactory
import com.google.auto.factory.Provided
import io.github.feelfreelinux.wykopmobilny.api.links.LinksApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.ui.modules.NewNavigatorApi

@AutoFactory
class LinkPresenter(
        @Provided val schedulers: Schedulers,
        @Provided val navigatorApi: NewNavigatorApi,
        @Provided val linksApi: LinksApi) : BasePresenter<LinkView>() {
    var linkId = -1

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
                            view?.showDigged()
                        }, {
                            view?.showErrorDialog(it)
                        })
        )
    }
}