package io.github.feelfreelinux.wykopmobilny.api.scraper

import io.github.feelfreelinux.wykopmobilny.models.scraper.Blacklist
import io.reactivex.Single

interface ScraperApi {
    fun getBlacklist() : Single<Blacklist>
}