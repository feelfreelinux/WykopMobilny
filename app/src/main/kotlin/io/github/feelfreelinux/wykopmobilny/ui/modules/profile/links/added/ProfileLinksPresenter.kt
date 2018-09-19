package io.github.feelfreelinux.wykopmobilny.ui.modules.profile.links.added

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Link
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinkActionListener
import io.github.feelfreelinux.wykopmobilny.ui.fragments.links.LinksInteractor
import io.reactivex.Single

class ProfileLinksPresenter(
    val schedulers: Schedulers,
    val profileApi: ProfileApi,
    val linksInteractor: LinksInteractor
) : BasePresenter<ProfileLinksView>(), LinkActionListener {

    var page = 1
    lateinit var username: String

    fun loadAdded(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
            profileApi.getAdded(username, page)
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
        )
    }

    fun loadBurried(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
            profileApi.getBuried(username, page)
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
        )
    }

    fun loadDigged(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
            profileApi.getDigged(username, page)
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
        )
    }

    fun loadPublished(shouldRefresh: Boolean) {
        if (shouldRefresh) page = 1
        compositeObservable.add(
            profileApi.getPublished(username, page)
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
        )
    }

    override fun dig(link: Link) {
        linksInteractor.dig(link).processLinkSingle(link)
    }

    override fun removeVote(link: Link) {
        linksInteractor.voteRemove(link).processLinkSingle(link)
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