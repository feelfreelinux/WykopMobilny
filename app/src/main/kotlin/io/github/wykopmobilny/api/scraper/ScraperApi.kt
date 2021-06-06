package io.github.wykopmobilny.api.scraper

import io.github.wykopmobilny.blacklist.api.Blacklist
import io.reactivex.Single

interface ScraperApi {
    fun getBlacklist(): Single<Blacklist>
}
