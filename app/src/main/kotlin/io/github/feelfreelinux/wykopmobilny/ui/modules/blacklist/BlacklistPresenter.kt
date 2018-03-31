package io.github.feelfreelinux.wykopmobilny.ui.modules.blacklist

import io.github.feelfreelinux.wykopmobilny.api.profile.ProfileApi
import io.github.feelfreelinux.wykopmobilny.api.scraper.ScraperApi
import io.github.feelfreelinux.wykopmobilny.api.tag.TagApi
import io.github.feelfreelinux.wykopmobilny.base.BasePresenter
import io.github.feelfreelinux.wykopmobilny.base.Schedulers

class BlacklistPresenter(val schedulers : Schedulers, val scraperApi: ScraperApi, val tagApi: TagApi, val profileApi: ProfileApi) : BasePresenter<BlacklistView>() {
    fun importBlacklist(sessionCookies : String) {
        compositeObservable.add(
                scraperApi.getBlacklist(sessionCookies)
                        .subscribeOn(schedulers.backgroundThread())
                        .observeOn(schedulers.mainThread())
                        .subscribe({ view?.importBlacklist(it) }, { view?.showErrorDialog(it) })
        )
    }
}