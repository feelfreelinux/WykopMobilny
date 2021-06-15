package io.github.wykopmobilny.api.scraper

import io.github.wykopmobilny.blacklist.api.ApiBlacklist
import io.reactivex.Single

interface ScraperApi {
    fun getBlacklist(): Single<ApiBlacklist>
}
