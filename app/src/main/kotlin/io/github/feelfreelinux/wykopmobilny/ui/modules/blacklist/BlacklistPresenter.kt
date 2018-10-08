package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.github.feelfreelinux.wykopmobilny.utils.intoComposite

class BlacklistPresenter(
    val schedulers: Schedulers,
    val scraperApi: ScraperApi,
    val tagApi: TagApi,
    val profileApi: ProfileApi
) : BasePresenter<BlacklistView>() {

    fun importBlacklist() {
        scraperApi.getBlacklist()
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.importBlacklist(it) }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun blockTag(tag: String) {
        tagApi.block(tag)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.refreshResults() },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    fun unblockTag(tag: String) {
        tagApi.unblock(tag)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe(
                { view?.refreshResults() },
                { view?.showErrorDialog(it) }
            )
            .intoComposite(compositeObservable)
    }

    fun blockUser(nick: String) {
        profileApi.block(nick)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.refreshResults() }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }

    fun unblockUser(nick: String) {
        profileApi.unblock(nick)
            .subscribeOn(schedulers.backgroundThread())
            .observeOn(schedulers.mainThread())
            .subscribe({ view?.refreshResults() }, { view?.showErrorDialog(it) })
            .intoComposite(compositeObservable)
    }
}