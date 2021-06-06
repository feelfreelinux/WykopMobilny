package io.github.wykopmobilny.api.scraper

import io.github.wykopmobilny.blacklist.api.Blacklist
import io.github.wykopmobilny.blacklist.api.ScraperRetrofitApi
import io.reactivex.Single
import javax.inject.Inject

class ScraperRepository @Inject constructor(
    private val scraperApi: ScraperRetrofitApi
) : ScraperApi {

    override fun getBlacklist(): Single<Blacklist> =
        scraperApi.getBlacklist()
}
